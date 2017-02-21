package slick

import com.github.stonexx.slick.ext.HasSlickProfile
import models.Cat
import slick.lifted.ProvenShape

trait CatsComponent { this: HasSlickProfile[MyPostgresProfile] with CommonComponent =>
  import profile.api._

  class Cats(tag: Tag) extends Table[Cat](tag, "cats") {

    def name: Rep[String] = column[String]("name", O.PrimaryKey)
    def color: Rep[String] = column[String]("color")
    def flag: Rep[Boolean] = column[Boolean]("flag", O.Default(true))
    def state: Rep[Cat.State.Value] = column[Cat.State.Value]("state", O.Length(1, varying = false), O.Default(Cat.State.Normal))

    def * : ProvenShape[Cat] = (name, color, flag, state) <> ((Cat.apply _).tupled, Cat.unapply)
  }
}
