package com.github.stonexx.scala.util

import java.io.{PrintWriter, StringWriter}

final class ThrowableOps(val self: Throwable) extends AnyVal {

  def stackTraceString: String = {
    val stackTrace = new StringWriter
    self.printStackTrace(new PrintWriter(stackTrace))
    stackTrace.toString
  }
}

trait ToThrowableOps {
  implicit def toThrowableOps(x: Throwable): ThrowableOps = new ThrowableOps(x)
}
