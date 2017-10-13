package dao

import javax.inject.{Inject, Singleton}

import models.Cat
import org.reactivestreams.Publisher
import play.api.db.slick.{HasDatabaseConfigProvider, DatabaseConfigProvider}
import slick.{MyPostgresProfile, CatsComponent}

import scala.concurrent.Future

@Singleton
class CatDao @Inject()(
  protected val dbConfigProvider: DatabaseConfigProvider
) extends HasDatabaseConfigProvider[MyPostgresProfile]
  with CatsComponent {

  import profile.api._

  private val cats = TableQuery[Cats]

  def list: Future[Seq[Cat]] = db.run(cats.result)

  def stream: Publisher[Cat] = db.stream(cats.result)

  def insert(cat: Cat): Future[Int] = db.run(cats += cat)

  def insert(cats: Seq[Cat]): Future[Option[Int]] = db.run(this.cats ++= cats)

  def delete(name: String): Future[Int] = db.run(cats.filter(_.name === name).delete)
}
