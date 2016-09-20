package com.github.stonexx.scala.util

object HangulCharOps {
  final val Blank    = ' '
  final val Initials = Array('ㄱ', 'ㄲ', 'ㄴ', 'ㄷ', 'ㄸ', 'ㄹ', 'ㅁ', 'ㅂ', 'ㅃ', 'ㅅ', 'ㅆ', 'ㅇ', 'ㅈ', 'ㅉ', 'ㅊ', 'ㅋ', 'ㅌ', 'ㅍ', 'ㅎ')
  final val Vowels   = Array('ㅏ', 'ㅐ', 'ㅑ', 'ㅒ', 'ㅓ', 'ㅔ', 'ㅕ', 'ㅖ', 'ㅗ', 'ㅘ', 'ㅙ', 'ㅚ', 'ㅛ', 'ㅜ', 'ㅝ', 'ㅞ', 'ㅟ', 'ㅠ', 'ㅡ', 'ㅢ', 'ㅣ')
  final val Unders   = Array(Blank, 'ㄱ', 'ㄲ', 'ㄳ', 'ㄴ', 'ㄵ', 'ㄶ', 'ㄷ', 'ㄹ', 'ㄺ', 'ㄻ', 'ㄼ', 'ㄽ', 'ㄾ', 'ㄿ', 'ㅀ', 'ㅁ', 'ㅂ', 'ㅄ', 'ㅅ', 'ㅆ', 'ㅇ', 'ㅈ', 'ㅊ', 'ㅋ', 'ㅌ', 'ㅍ', 'ㅎ')
}

final class HangulCharOps(val self: Char) extends AnyVal {
  import HangulCharOps._

  @inline def isHangul: Boolean = self >= 0xAC00 && self <= 0xD7A3

  @inline def hangulInitial: Option[Char] = if (isHangul) Some(Initials(((self & 0xFFFF) - 0xAC00) / (21 * 28))) else None

  @inline def hangulVowel: Option[Char] = if (isHangul) Some(Vowels((((self & 0xFFFF) - 0xAC00) % (21 * 28)) / 28)) else None

  @inline def hangulUnder: Option[Char] = if (isHangul) Some(Unders((((self & 0xFFFF) - 0xAC00) % (21 * 28)) % 28)).filter(_ != Blank) else None

  @inline def hasHangulUnder: Boolean = hangulUnder.isDefined
}

trait ToHangulCharOps {
  implicit def toHangulCharOps(x: Char): HangulCharOps = new HangulCharOps(x)
}
