package com.github.stonexx.scala.util

import java.io.{PrintWriter, File}

import org.scalatest._

class FileOpsSpec extends FlatSpec with Matchers {
  import file._

  "#nonComflictFile" should "중복되지않는 파일 생성" in {
    val tmpDir = new File(System.getProperty("java.io.tmpdir"))
    val tmpFile = tmpDir.nonComflictFile

    tmpFile.exists shouldBe false
    new PrintWriter(tmpFile) {
      write("This is temp file.")
      close()
    }
    tmpFile.nonComflictFile.exists shouldBe false

    val testFile = new File(tmpDir, "test.txt").nonComflictFile
    testFile.exists shouldBe false
    new PrintWriter(testFile) {
      write("This is test file.")
      close()
    }
    testFile.nonComflictFile.exists shouldBe false
  }
}
