package com.github.stonexx.scala.util

import org.scalatest._

class HangulCharOpsSpec extends FlatSpec with Matchers {
  import hangul.char._

  "#isHangul" should "문자가 한글인지 검사" in {
    '한'.isHangul shouldBe true
    '글'.isHangul shouldBe true
    'a'.isHangul shouldBe false
  }

  "#hangulInitial" should "한글 문자의 초성을 분리" in {
    '한'.hangulInitial shouldBe Some('ㅎ')
    '글'.hangulInitial shouldBe Some('ㄱ')
    'a'.hangulInitial shouldBe None
  }

  "#hangulVowel" should "한글 문자의 중성을 분리" in {
    '한'.hangulVowel shouldBe Some('ㅏ')
    '글'.hangulVowel shouldBe Some('ㅡ')
    'a'.hangulVowel shouldBe None
  }

  "#hangulUnder" should "한글 문자의 종성을 분리" in {
    '한'.hangulUnder shouldBe Some('ㄴ')
    '글'.hangulUnder shouldBe Some('ㄹ')
    '이'.hangulUnder shouldBe None
    'a'.hangulUnder shouldBe None
  }

  "#hasHangulUnder" should "한글 문자의 종성이 있는지 검사" in {
    '한'.hasHangulUnder shouldBe true
    '글'.hasHangulUnder shouldBe true
    '이'.hasHangulUnder shouldBe false
    'a'.hasHangulUnder shouldBe false
  }

}

