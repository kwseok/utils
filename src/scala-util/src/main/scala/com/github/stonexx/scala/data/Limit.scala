package com.github.stonexx.scala.data

case class Limit[A](from: Option[A] = None, to: Option[A] = None) {
  def isEmpty = from.isEmpty && to.isEmpty
  def isDefined = !isEmpty

  def apply[B](from: A => B, to: A => B): Seq[B] =
    this.from.map(from).toList ::: this.to.map(to).toList

  def apply[B](from: A => B, to: A => B, eq: A => B): Seq[B] =
    if (this.from == this.to) this.from.map(eq).toList else apply(from, to)
}
