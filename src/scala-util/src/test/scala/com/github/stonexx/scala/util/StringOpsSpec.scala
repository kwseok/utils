package com.github.stonexx.scala.util

import java.nio.charset.Charset
import java.util.{Calendar, Locale}

import org.htmlcleaner.{CleanerProperties, HtmlSerializer, PrettyHtmlSerializer}
import org.scalatest._

class StringOpsSpec extends FlatSpec with Matchers {
  import string._

  "#isDigits" should "문자열이 모두 숫자형식의 문자로 이루어졌는지 검사" in {
    "123467890".isDigits shouldBe true
    "12346789a".isDigits shouldBe false
  }

  "#uncapitalize" should "문자열의 첫번째 문자를 소문자로 변경" in {
    "Cat".uncapitalize shouldBe "cat"
    "CAT".uncapitalize shouldBe "cAT"
  }

  "#camelToHyphen" should "카멜 케이스 형식의 문자열을 하이픈 형식으로 변경" in {
    "itIsTest".camelToHyphen shouldBe "it-is-test"
    "ItIsTest".camelToHyphen shouldBe "it-is-test"
  }

  "#camelToUnderscore" should "카멜 케이스 형식의 문자열을 언더스코어 형식으로 변경" in {
    "itIsTest".camelToUnderscore shouldBe "it_is_test"
    "ItIsTest".camelToUnderscore shouldBe "it_is_test"
  }

  "#hyphenToCamel" should "하이픈 형식의 문자열을 카멜 케이스 형식으로 변경" in {
    "it-is-test".hyphenToCamel shouldBe "itIsTest"
    "It-Is-Test".hyphenToCamel shouldBe "itIsTest"
  }

  "#hyphenToUnderscore" should "하이픈 형식의 문자열을 언더스코어 형식으로 변경" in {
    "it-is-test".hyphenToUnderscore shouldBe "it_is_test"
    "It-Is-Test".hyphenToUnderscore shouldBe "It_Is_Test"
  }

  "#underscoreToCamel" should "언더스코어 형식의 문자열을 카멜 케이스 형식으로 변경" in {
    "it_is_test".underscoreToCamel shouldBe "itIsTest"
    "It_Is_Test".underscoreToCamel shouldBe "itIsTest"
  }

  "#underscoreToHyphen" should "언더스코어 형식의 문자열을 하이픈 형식으로 변경" in {
    "it_is_test".underscoreToHyphen shouldBe "it-is-test"
    "It_Is_Test".underscoreToHyphen shouldBe "It-Is-Test"
  }

  "#toAntStylePattern" should "Ant 스타일 패턴의 문자열을 Regex로 변경" in {
    "/test/**".toAntStylePattern.findFirstIn("/test/path/abc") shouldBe Some("/test/path/abc")
    "/test/*/aaa".toAntStylePattern.findFirstIn("/test/path/to/aaa") shouldBe None
    "/test/*/abc.txt".toAntStylePattern.findFirstIn("/test/path/abc.txt") shouldBe Some("/test/path/abc.txt")
    "/test/*/abc?.txt".toAntStylePattern.findFirstIn("/test/path/abc1.txt") shouldBe Some("/test/path/abc1.txt")
    "/test/**/zzz.txt".toAntStylePattern.findFirstIn("/test/path/to/a/b/zzz.txt") shouldBe Some("/test/path/to/a/b/zzz.txt")
  }

  "#prependEachLine" should "각 행의 앞에 주어진 문자열을 덧 붙인다" in {
    val multiline =
      """aaaaaaaaaaaaaaaaaa
        |bbbbbbbbbbbbbbbbbb
        |cccccccccccccccccc
        |dddddddddddddddddd
        |eeeeeeeeeeeeeeeeee
      """.stripMargin.trim
    val prependedMultiline =
      """>>> aaaaaaaaaaaaaaaaaa
        |>>> bbbbbbbbbbbbbbbbbb
        |>>> cccccccccccccccccc
        |>>> dddddddddddddddddd
        |>>> eeeeeeeeeeeeeeeeee
      """.stripMargin.trim
    multiline.prependEachLine(">>> ") shouldBe prependedMultiline
  }

  "#stripTags" should "XML 태그를 모두 제거한다" in {
    val xml =
      """<items>
        |  <item id="1">아이템1</item>
        |  <item id="2">아이템2</item>
        |  <item id="3">아이템3</item>
        |</items>
      """.stripMargin
    val striptedXml =
      """
        |  아이템1
        |  아이템2
        |  아이템3
        |
      """.stripMargin
    xml.stripTags shouldBe striptedXml
  }

  "#cutstring" should "문자열을 주어진 길이만큼 잘라낸다" in {
    "가나다라마바사아자차카타파하".cutstring(10) shouldBe "가나다라마바사아자차"
    "가나다라마바사아자차카타파하".cutstring(10, "...") shouldBe "가나다라마바사..."
    "abcdefghijklmnopqrstuvwxyz".cutstring(10) shouldBe "abcdefghij"
    "abcdefghijklmnopqrstuvwxyz".cutstring(10, "...") shouldBe "abcdefg..."
    "a가b나c다d라e마f바g사h아i자j차k카l타m파n하o".cutstring(10) shouldBe "a가b나c다d라e마"
    "a가b나c다d라e마f바g사h아i자j차k카l타m파n하o".cutstring(10, "...") shouldBe "a가b나c다d..."
  }

  "#parseDate" should "날짜형식의 문자열을 java.util.Date 형식으로 변경" in {
    val calendar = Calendar.getInstance(Locale.KOREA)
    calendar.set(2014, 6, 27, 18, 51, 39)
    calendar.set(Calendar.MILLISECOND, 123)
    "2014-07-27 오후 18:51:39 123".parseDate("yyyy-MM-dd a HH:mm:ss SSS", locale = Locale.KOREA) shouldBe calendar.getTime
  }

  "#parseMap for Scala Map" should "스칼라 맵 형식의 문자열을 스칼라 맵으로 파싱하여 변경한다" in {
    val map = Map(
      "test1" -> "value1",
      "test2" -> "value2",
      "test3" -> "value3"
    )
    val mapString = map.toString()
    mapString.parseMap shouldBe map
  }

  "#parseMap for Java Map" should "자바 맵 형식의 문자열을 스칼라 맵으로 파싱하여 변경한다" in {
    import scala.collection.JavaConverters._
    val map = new java.util.HashMap[String, String]
    map.put("test1", "value1")
    map.put("test2", "value2")
    map.put("test3", "value3")
    val mapString = map.toString
    mapString.parseMap shouldBe map.asScala
  }

  "#checkTranscoding" should "문자열이 주어진 인코딩으로 변경이 가능한지 검사" in {
    "가힣나똫라뿧마".checkTranscoding(Charset.forName("EUC-KR")) shouldBe Set("힣", "똫", "뿧")
  }

  "#encryptMD5" should "문자열을 MD5 알고리즘을 사용하여 암호화" in {
    "1234".encryptMD5 shouldBe "1234".encryptMD5
    "1234".encryptMD5 shouldNot be("4321".encryptMD5)
  }

  "#encryptSHA" should "문자열을 SHA 알고리즘을 사용하여 암호화" in {
    "1234".encryptSHA shouldBe "1234".encryptSHA
    "1234".encryptSHA shouldNot be("4321".encryptSHA)
  }

  "#encryptSHA256" should "문자열을 SHA256 알고리즘을 사용하여 암호화" in {
    "1234".encryptSHA256 shouldBe "1234".encryptSHA256
    "1234".encryptSHA256 shouldNot be("4321".encryptSHA256)
  }

  "#encryptSHA384" should "문자열을 SHA384 알고리즘을 사용하여 암호화" in {
    "1234".encryptSHA384 shouldBe "1234".encryptSHA384
    "1234".encryptSHA384 shouldNot be("4321".encryptSHA384)
  }

  "#encryptSHA512" should "문자열을 SHA512 알고리즘을 사용하여 암호화" in {
    "1234".encryptSHA512 shouldBe "1234".encryptSHA512
    "1234".encryptSHA512 shouldNot be("4321".encryptSHA512)
  }

  "#nl2br" should "개행문자를 <br/> 로 변경" in {
    "aaaaa\nbbbbb\r\nccccc\nddddd\r\n".nl2br shouldBe "aaaaa<br/>bbbbb<br/>ccccc<br/>ddddd<br/>"
  }

  "#br2nl" should "<br/> 문자를 개행문자로 변경" in {
    "aaaaa<br/>bbbbb<br>ccccc<br/>ddddd<br>".br2nl shouldBe "aaaaa\nbbbbb\nccccc\nddddd\n"
  }

  "#space2nbsp" should "공백문자를 &nbsp; 문자로 변경" in {
    "aaaaa bbbbb  ccccc   ddddd ".space2nbsp shouldBe "aaaaa&nbsp;bbbbb&nbsp;&nbsp;ccccc&nbsp;&nbsp;&nbsp;ddddd&nbsp;"
  }

  "#nbsp2space" should "&nbsp; 문자를 공백문자로 변경" in {
    "aaaaa&nbsp;bbbbb&nbsp;&nbsp;ccccc&nbsp;&nbsp;&nbsp;ddddd&nbsp;".nbsp2space shouldBe "aaaaa bbbbb  ccccc   ddddd "
  }

  "#htmlArrange" should "html을 정리" in {
    val serializer: CleanerProperties => HtmlSerializer = new PrettyHtmlSerializer(_)
    val html =
      """<div>
        |  <span id  =  test>테스트
        |</div>
      """.stripMargin
    val arrangedHtml = "\n\t<div>\n\t\t<span id=\"test\">테스트</span>\n\t</div>\n"
    html.htmlArrange("body", innerHtml = true, serializer = serializer) shouldBe arrangedHtml
  }

  "#htmlCleanXSS" should "html 의 XSS(cross site scripting) 정리" in {
    val html =
      """<html>
        |<head>
        |  <style type="text/css"></style>
        |  <script type="text/javascript">
        |    alert(1);
        |  </script>
        |</head>
        |<body>
        |  <div>
        |    <a href="javascript:alert(2);" style="width: expression(alert(5));">test</a>
        |    <img SRC="javascript:alert(3);"/>
        |    <iframe src="javascript:alert(4);"></iframe>
        |    <div style="background-image: url(javascript:alert(6));"></div>
        |    <div style="background-image: url(javascript:eval('alert(7)'));"></div>
        |    <p>테스트</p>
        |  </div>
        |</body>
        |</html>
      """.stripMargin
    val cleanedHtml = "<html>\n <head></head>\n <body>       \n  <a rel=\"nofollow\">test</a>     \n  <p>테스트</p>    \n </body>\n</html>"
    html.htmlCleanXSS() shouldBe cleanedHtml
  }
}
