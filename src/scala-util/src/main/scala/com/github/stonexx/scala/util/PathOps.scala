package com.github.stonexx.scala.util

import java.nio.file.Files
import java.nio.file.Path

import org.apache.commons.io.FilenameUtils.{getBaseName, getExtension}
import org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric

import scala.util.matching.Regex.Groups

object PathOps {
  final val RegexNumberedBaseName = """(.*)\((\d+)\)$""".r
}

final class PathOps(val self: Path) extends AnyVal {
  import PathOps._

  def nonComflictPath: Path = {
    var path = if (Files.isDirectory(self)) self.resolve(randomAlphanumeric(8)) else self
    if (Files.exists(path)) {
      val (basename, beginCount, suffix) = {
        val filename = path.getFileName.toString
        val (maybeNumberedBasename, suffix) = Option(getBaseName(filename)).filter(_.nonEmpty) match {
          case Some(name) => name -> Option(getExtension(filename)).filter(_.nonEmpty).map("." + _).getOrElse("")
          case None => filename -> ""
        }
        RegexNumberedBaseName.findFirstMatchIn(maybeNumberedBasename) match {
          case Some(Groups(name, cnt)) => (name, cnt.toInt, suffix)
          case None => (maybeNumberedBasename, 1, suffix)
        }
      }
      var count = beginCount
      do {
        path = path.resolveSibling(s"$basename($count)$suffix")
        count += 1
      } while (Files.exists(path))
    }
    path
  }
}

trait ToPathOps {
  implicit def toPathOps(x: Path): PathOps = new PathOps(x)
}
