package com.github.stonexx.scala.util

import java.nio.charset.Charset
import java.security.MessageDigest
import java.text.SimpleDateFormat
import java.util.{TimeZone, Locale, Date}

import com.google.common.base.CaseFormat
import org.apache.commons.codec.binary.Hex
import org.apache.commons.lang3.StringUtils
import org.htmlcleaner._
import org.jsoup.Jsoup
import org.jsoup.safety.{Cleaner, Whitelist}

import scala.util.matching.Regex

object StringOps {
  final val RegexDuplicatedSlash                   = """/{2,}""".r
  final val RegexAntStylePatternSpecialCharacters  = """[-\[\]{}()+.,\\^$|#\s]""".r
  final val RegexAntStylePatternWildcardsWithSlash = """(/?\*\*)|(/?\*)|(\?)|(/)""".r
  final val RegexBeginningOfEachLine               = """(?m)^""".r
  final val RegexAllTags                           = """<[/\!]*?[^<>]*?>""".r
  final val RegexSpecialRegexChars                 = "[{}()\\[\\].+*?^$\\\\|]".r

  def defaultHtmlCleanerProperties: CleanerProperties = {
    val props = new CleanerProperties()
    props.setAllowHtmlInsideAttributes(true)
    props.setRecognizeUnicodeChars(false)
    props.setOmitXmlDeclaration(true)
    props
  }
}

final class StringOps(val self: String) extends AnyVal {
  import StringOps._

  @inline def isDigits: Boolean = self.nonEmpty && self.forall(_.isDigit)

  @inline def uncapitalize: String = StringUtils.uncapitalize(self)

  @inline def camelToHyphen: String = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_HYPHEN, self)
  @inline def camelToUnderscore: String = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, self)

  @inline def hyphenToCamel: String = CaseFormat.LOWER_HYPHEN.to(CaseFormat.LOWER_CAMEL, self)
  @inline def hyphenToUnderscore: String = CaseFormat.LOWER_HYPHEN.to(CaseFormat.LOWER_UNDERSCORE, self)

  @inline def underscoreToCamel: String = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, self)
  @inline def underscoreToHyphen: String = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_HYPHEN, self)

  def toAntStylePattern: Regex = {
    val cleanedPath = RegexDuplicatedSlash.replaceAllIn(self, "/")
    val escapedPath = RegexAntStylePatternSpecialCharacters.replaceAllIn(cleanedPath, "\\\\$0")
    val pattern = RegexAntStylePatternWildcardsWithSlash.replaceAllIn(escapedPath, {
      _.group(0) match {
        case "/**" => "(?:/{1,}.*)?"
        case "**" => ".*"
        case "/*" => "(?:/{1,}[^/]*)?"
        case "*" => "[^/]*"
        case "?" => "[^/]"
        case "/" => "/{1,}"
      }
    })
    ("^(" + pattern + ")$").r
  }

  /** Shortcut for [[com.github.stonexx.scala.util.StringOps#toAntStylePattern]] */
  @inline def a: Regex = toAntStylePattern

  @inline def prependEachLine(prefix: String): String = RegexBeginningOfEachLine.replaceAllIn(self, prefix)

  @inline def stripTags: String = RegexAllTags.replaceAllIn(self, "")

  @inline def escapeRegex(s: String): String = RegexSpecialRegexChars.replaceAllIn(s, "\\\\$0")

  @inline def cutstring(length: Int, suffix: String = ""): String =
    if (self.length <= length - suffix.length) self
    else self.substring(0, length - suffix.length) + suffix

  def parseDate[T <: String](
    pattern: T,
    timeZone: TimeZone = TimeZone.getDefault,
    locale: Locale = Locale.getDefault
  ): Date = {
    val parser = new SimpleDateFormat(pattern, locale)
    parser.setLenient(true)
    parser.setTimeZone(timeZone)
    parser.parse(self)
  }

  def parseMap(trimValue: Boolean): Map[String, String] = if (self.isEmpty) Map.empty
  else {
    val isScalaMap = self.startsWith("Map(")
    val headOffset = if (isScalaMap) 4 else if (self.head == '{') 1 else 0
    val lastOffset = if (")}" contains self.last) 1 else 0
    val mapEq = if (isScalaMap) " -> " else "="
    val mapString = self.slice(headOffset, self.length - lastOffset)

    mapString.split(", ").map { pair =>
      pair.split(mapEq, 2) match {
        case Array(key, value) => key -> (if (trimValue) value.trim else value)
        case _ =>
          throw new IllegalArgumentException("No " + mapEq + " after " + pair)
      }
    }.toMap
  }

  @inline def parseMap: Map[String, String] = parseMap(false)

  def checkTranscoding(encoding: Charset): Set[String] = (Set.empty[String] /: self) { (z, c) =>
    val s = String.valueOf(c)
    val convertedChar = new String(new String(s.getBytes(encoding), encoding).getBytes)
    if (s != convertedChar) z + s else z
  }

  @inline def checkTranscoding: Set[String] = checkTranscoding(Charset.defaultCharset)

  def encrypt(algorithm: String): String = {
    val md = MessageDigest.getInstance(algorithm)
    md.update(self.getBytes)
    new String(Hex encodeHex md.digest)
  }
  @inline def encryptMD5: String = encrypt("MD5")
  @inline def encryptSHA: String = encrypt("SHA")
  @inline def encryptSHA256: String = encrypt("SHA-256")
  @inline def encryptSHA384: String = encrypt("SHA-384")
  @inline def encryptSHA512: String = encrypt("SHA-512")

  @inline def nl2br: String = StringUtils.replaceEach(self, Array("\r\n", "\n"), Array("<br/>", "<br/>"))
  @inline def br2nl: String = StringUtils.replaceEach(self, Array("<br/>", "<br>"), Array("\n", "\n"))
  @inline def space2nbsp: String = StringUtils.replace(self, " ", "&nbsp;")
  @inline def nbsp2space: String = StringUtils.replace(self, "&nbsp;", " ")

  def htmlArrange(
    parentTagName: String = "html",
    innerHtml: Boolean = false,
    props: CleanerProperties = defaultHtmlCleanerProperties,
    serializer: CleanerProperties => HtmlSerializer = new SimpleHtmlSerializer(_)
  ): String = {
    val cleaner = new HtmlCleaner(props)
    val ser = serializer(props)
    val rootNode = cleaner.clean(self)
    val innerNode = if (parentTagName.toLowerCase == "html") rootNode
    else rootNode.findElementByName(parentTagName, true)
    if (innerNode == null) ""
    else {
      val html = ser.getAsString(innerNode)
      if (innerHtml) {
        val begin = html.indexOf('>', html.indexOf("<" + innerNode.getName) + 1)
        val end = html.lastIndexOf('<')
        if (begin >= 0 && begin <= end) html.substring(begin + 1, end) else ""
      } else html
    }
  }

  def htmlCleanXSS(baseUri: String = "", whitelist: Whitelist = Whitelist.basic): String = {
    val dirty = Jsoup.parseBodyFragment(self, baseUri)
    val cleaner = new Cleaner(whitelist)
    val clean = cleaner.clean(dirty)
    clean.html
  }
}

trait ToStringOps {
  implicit def toStringOps(x: String): StringOps = new StringOps(x)
}
