package dao

import javax.inject.{Inject, Singleton}

import com.github.stonexx.play.db.slick.Database
import models.Task

import scala.concurrent.Future

@Singleton
class TaskDao @Inject()(db: Database) {
  import slick.Tables._, profile.api._

  private val tasks = TableQuery[Tasks]

  def list: Future[Seq[Task]] = db.run(tasks.result)

  def existsByLabel(label: String, exceptId: Option[Long] = None): Future[Boolean] = db.run {
    tasks.filter { t =>
      (t.label === label) :: exceptId.map(t.id.? =!= _).toList reduce (_ && _)
    }.exists.result
  }

  def insert(label: String): Future[Int] = db.run(tasks += Task(None, Some(label)))

  def delete(id: Long): Future[Int] = db.run(tasks.filter(_.id === id).delete)
}
