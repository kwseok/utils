package com.github.stonexx.scala.util

import java.io.File

final class FileOps(val self: File) extends AnyVal {
  import path._

  @inline def nonComflictFile: File = self.toPath.nonComflictPath.toFile
}

trait ToFileOps {
  implicit def toFileOps(x: File): FileOps = new FileOps(x)
}
