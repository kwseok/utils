package slick.table

import com.github.stonexx.slick.ext.HasSlickProfile
import models.Task
import slick.MyPostgresProfile

trait TasksComponent { this: HasSlickProfile[MyPostgresProfile] =>
  import profile.api._

  class Tasks(tag: Tag) extends Table[Task](tag, "tasks") {

    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def label = column[Option[String]]("label")

    def * = (id.?, label) <> ((Task.apply _).tupled, Task.unapply)
  }
}
