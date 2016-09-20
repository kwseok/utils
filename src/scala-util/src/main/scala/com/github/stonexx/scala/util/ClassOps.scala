package com.github.stonexx.scala.util

import java.lang.annotation.Annotation
import java.net.URI
import java.util.{Date, Locale}

import org.apache.commons.lang3.ClassUtils

import scala.reflect.ClassTag

final class ClassOps[T](val self: Class[T]) extends AnyVal {
  import cls._

  def isSimpleType: Boolean = ClassUtils.isPrimitiveOrWrapper(self) ||
    classOf[CharSequence].isAssignableFrom(self) ||
    classOf[Number].isAssignableFrom(self) ||
    classOf[Date].isAssignableFrom(self) ||
    classOf[URI] == self ||
    classOf[Locale] == self ||
    classOf[Class[_]] == self ||
    self.isEnum

  def findAnnotation[A <: Annotation : ClassTag](annotationClass: Class[A]): Option[A] =
    Option(self getAnnotation annotationClass).orElse {
      self.getInterfaces.find(_ isAnnotationPresent annotationClass).flatMap(_.findAnnotation[A])
    }.orElse {
      if (classOf[Annotation] isAssignableFrom self) None
      else self.getAnnotations.map(_.annotationType).find(_ isAnnotationPresent annotationClass).flatMap(_.findAnnotation[A])
    }.orElse {
      Option(self.getSuperclass).filter(_ != classOf[Object]).flatMap(_.findAnnotation[A])
    }

  @inline
  def findAnnotation[A <: Annotation : ClassTag]: Option[A] = findAnnotation(TypeUtils.runtimeClass[A])
}

trait ToClassOps {
  implicit def toClassOps[T](x: Class[T]): ClassOps[T] = new ClassOps(x)
}
