package com.github.stonexx.scala.util

object HangulStringOps {
  final val Numbers = Array("영", "일", "이", "삼", "사", "오", "육", "칠", "팔", "구")
  final val Units   = Array(Array("십", "백", "천"), Array("만", "억", "조", "경"))
}

final class HangulStringOps(val self: String) extends AnyVal {
  import HangulStringOps._
  import hangul.char._
  import string._

  @inline def isHangul: Boolean = self.nonEmpty && self.forall(_.isHangul)

  def toHangulNumber(delimiter: String = ""): String = if (!self.isDigits) self
  else {
    val digits = self.dropWhile(_ == '0').map(_ - '0')
    if (digits.isEmpty) return Numbers(0)
    if (digits.length == 1) return Numbers(digits.head)
    digits.reverseIterator.grouped(4).zipWithIndex.map {
      case (groupedDigits, i) =>
        val hangulDigits = (for {
          (n, j) <- groupedDigits.zipWithIndex
          hn = if (n > 1 || j == 0 && n == 1) Numbers(n) else ""
          hu = if (n > 0 && j > 0) Units(0)(j - 1) else ""
        } yield hn + hu).reduceRight { (l, r) => r + l }
        val largeUnit = if (i > 0 && hangulDigits.nonEmpty) Units(1)((i - 1) % 4) else ""
        hangulDigits + largeUnit
    }.filter(_.nonEmpty).reduceRight { (l, r) => r + delimiter + l }
  }

  @inline def toHangulNumber: String = toHangulNumber()
}

trait ToHangulStringOps {
  implicit def toHangulStringOps(x: String): HangulStringOps = new HangulStringOps(x)
}
