package com.github.stonexx.scala.data

case class Page[A](items: Seq[A], page: Int, size: Int, total: Int) {
  require(page > 0)
  require(size >= 0)
  require(total >= 0)

  lazy val offset: Long = (page - 1) * size

  lazy val last: Int = size match {
    case 0 => 1
    case s => Math.ceil(total / s.toDouble).toInt
  }

  lazy val prev: Option[Int] = Option(page - 1).filter(_ >= 1)

  lazy val next: Option[Int] = Option(page + 1).filter(_ <= last)

  def foreach[B](f: A => Unit): Unit = items.foreach(f)

  def map[B](f: A => B): Page[B] = this.copy(items = items.map(f))
}

object Page {
  object Result {
    def unapply[A](page: Page[A]) = Some((
      page.items, page.page, page.size, page.total,
      page.offset, page.last, page.prev, page.next
    ))
  }
}
