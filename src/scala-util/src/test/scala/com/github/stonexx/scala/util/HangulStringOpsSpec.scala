package com.github.stonexx.scala.util

import org.scalatest._

class HangulStringOpsSpec extends FlatSpec with Matchers {
  import hangul.string._

  "#isHangul" should "문자열이 한글인지 검사" in {
    "한글".isHangul shouldBe true
    "한글a".isHangul shouldBe false
    "abc".isHangul shouldBe false
  }

  "#toHangulNumber" should "숫자형식의 문자열을 한글숫자로 변환" in {
    "123456789".toHangulNumber shouldBe "일억이천삼백사십오만육천칠백팔십구"
    "123456789".toHangulNumber(" ") shouldBe "일억 이천삼백사십오만 육천칠백팔십구"
    "12345678901234567890".toHangulNumber shouldBe "천이백삼십사경오천육백칠십팔조구천십이억삼천사백오십육만칠천팔백구십"
    "12345678901234567890".toHangulNumber(" ") shouldBe "천이백삼십사경 오천육백칠십팔조 구천십이억 삼천사백오십육만 칠천팔백구십"
    "11111111111111111111".toHangulNumber shouldBe "천백십일경천백십일조천백십일억천백십일만천백십일"
    "11111111111111111111".toHangulNumber(" ") shouldBe "천백십일경 천백십일조 천백십일억 천백십일만 천백십일"
    "10000000000000000000".toHangulNumber shouldBe "천경"
    "10000000000000000000".toHangulNumber(" ") shouldBe "천경"
    "00000000000000000001".toHangulNumber shouldBe "일"
    "00000000000000000001".toHangulNumber(" ") shouldBe "일"
    "00000000010000000000".toHangulNumber shouldBe "백억"
    "00000000010000000000".toHangulNumber(" ") shouldBe "백억"
    "10001000010000100001".toHangulNumber shouldBe "천경천조백억십만일"
    "10001000010000100001".toHangulNumber(" ") shouldBe "천경 천조 백억 십만 일"
    "20002000020000200002".toHangulNumber shouldBe "이천경이천조이백억이십만이"
    "20002000020000200002".toHangulNumber(" ") shouldBe "이천경 이천조 이백억 이십만 이"
    "abcdefghi".toHangulNumber shouldBe "abcdefghi"
  }

}
