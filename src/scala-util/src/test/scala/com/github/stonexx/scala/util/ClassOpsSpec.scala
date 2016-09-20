package com.github.stonexx.scala.util

import org.scalatest._

class ClassOpsSpec extends FlatSpec with Matchers {
  import cls._

  "#isSimpleType" should "심플 타입인지 검사" in {
    classOf[Boolean].isSimpleType shouldBe true
    classOf[Byte].isSimpleType shouldBe true
    classOf[Char].isSimpleType shouldBe true
    classOf[Short].isSimpleType shouldBe true
    classOf[Int].isSimpleType shouldBe true
    classOf[Long].isSimpleType shouldBe true
    classOf[Double].isSimpleType shouldBe true
    classOf[Float].isSimpleType shouldBe true
    classOf[java.lang.Boolean].isSimpleType shouldBe true
    classOf[java.lang.Byte].isSimpleType shouldBe true
    classOf[java.lang.Character].isSimpleType shouldBe true
    classOf[java.lang.Short].isSimpleType shouldBe true
    classOf[java.lang.Integer].isSimpleType shouldBe true
    classOf[java.lang.Long].isSimpleType shouldBe true
    classOf[java.lang.Double].isSimpleType shouldBe true
    classOf[java.lang.Float].isSimpleType shouldBe true
    classOf[CharSequence].isSimpleType shouldBe true
    classOf[Number].isSimpleType shouldBe true
    classOf[java.util.Date].isSimpleType shouldBe true
    classOf[java.net.URI].isSimpleType shouldBe true
    classOf[java.util.Locale].isSimpleType shouldBe true
    classOf[Class[_]].isSimpleType shouldBe true
    classOf[ClassOpsSpec].isSimpleType shouldBe false
  }

  "#findAnnotation" should "Java 어노테이션을 찾는다" in {
    @javax.xml.ws.Action(input = "INPUT", output = "OUTPUT")
    class Test

    classOf[Test].findAnnotation[javax.xml.ws.Action].get.input shouldBe "INPUT"
    classOf[Test].findAnnotation[javax.xml.ws.Action].get.output shouldBe "OUTPUT"
    classOf[Test].findAnnotation[javax.xml.ws.FaultAction] shouldBe None
  }
}
