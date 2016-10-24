package com.github.stonexx.scala.util

object ClassLoaderUtils {

  val defaultClassLoader: ClassLoader = new ClassLoader(this.getClass.getClassLoader) {
    override def loadClass(name: String): Class[_] = try {
      Thread.currentThread().getContextClassLoader.loadClass(name)
    } catch {
      case _: ClassNotFoundException => super.loadClass(name)
    }
  }
}
