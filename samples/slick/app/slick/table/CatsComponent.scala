package slick.table

import com.github.stonexx.slick.ext.HasSlickProfile
import models.Cat
import slick.MyPostgresProfile

trait CatsComponent { this: HasSlickProfile[MyPostgresProfile] with aux.ColumnTypesComponent =>
  import profile.api._

  class Cats(tag: Tag) extends Table[Cat](tag, "cats") {

    def name = column[String]("name", O.PrimaryKey)
    def color = column[String]("color")
    def flag = column[Boolean]("flag", O.Default(true))
    def state = column[Cat.State.Value]("state", O.Length(1, varying = false), O.Default(Cat.State.Normal))

    def * = (name, color, flag, state) <> ((Cat.apply _).tupled, Cat.unapply)
  }
}
