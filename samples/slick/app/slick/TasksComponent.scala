package slick

import com.github.stonexx.slick.ext.HasSlickProfile
import models.Task
import slick.lifted.ProvenShape

trait TasksComponent { this: HasSlickProfile[MyPostgresProfile] =>
  import profile.api._

  class Tasks(tag: Tag) extends Table[Task](tag, "tasks") {

    def id: Rep[Long] = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def label: Rep[Option[String]] = column[Option[String]]("label")

    def * : ProvenShape[Task] = (id.?, label) <> ((Task.apply _).tupled, Task.unapply)
  }
}
