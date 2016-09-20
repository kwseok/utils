package com.github.stonexx.scala.util

import java.io.PrintWriter
import java.nio.file.{Files, Paths}

import org.scalatest._

class PathOpsSpec extends FlatSpec with Matchers {
  import path._

  "#nonComflictFile" should "중복되지않는 파일 생성" in {
    val tmpDir = Paths.get(System.getProperty("java.io.tmpdir"))
    val tmpPath = tmpDir.nonComflictPath
    Files.exists(tmpPath) shouldBe false
    new PrintWriter(tmpPath.toFile) {
      write("This is temp file.")
      close()
    }
    Files.exists(tmpPath.nonComflictPath) shouldBe false

    val testPath = tmpDir.resolve("test.txt").nonComflictPath
    Files.exists(testPath) shouldBe false
    new PrintWriter(testPath.toFile) {
      write("This is test file.")
      close()
    }
    Files.exists(testPath.nonComflictPath) shouldBe false
  }
}
