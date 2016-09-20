package dao

import javax.inject.{Inject, Singleton}

import com.github.stonexx.play.db.slick.Database
import models.Cat
import org.reactivestreams.Publisher

import scala.concurrent.Future

@Singleton
class CatDao @Inject()(db: Database) {
  import slick.Tables._, profile.api._

  private val cats = TableQuery[Cats]

  def list: Future[Seq[Cat]] = db.run(cats.result)

  def stream: Publisher[Cat] = db.stream(cats.result)

  def insert(cat: Cat): Future[Int] = db.run(cats += cat)

  def insert(cats: Seq[Cat]): Future[Option[Int]] = db.run(this.cats ++= cats)

  def delete(name: String): Future[Int] = db.run(cats.filter(_.name === name).delete)
}
