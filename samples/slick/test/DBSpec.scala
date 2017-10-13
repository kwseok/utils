import controllers.Cats
import org.scalatestplus.play._
import play.api.Application
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import play.api.test._
import play.api.test.Helpers._

import scala.concurrent.ExecutionContext

/**
 * test the kitty cat database
 */
class DBSpec extends PlaySpec with guice.GuiceOneAppPerTest {

  class WithCatsComponent(protected val dbConfigProvider: DatabaseConfigProvider)
    extends slick.CatsComponent
      with HasDatabaseConfigProvider[slick.MyPostgresProfile]

  "DB" should {
    "work as expected" in new WithCatsComponent(app.injector.instanceOf[DatabaseConfigProvider]) {
      implicit val ec = app.injector.instanceOf[ExecutionContext]

      import profile.api._
      import models._

      val cats = TableQuery[Cats]

      val testKitties = Seq(
        Cat("kit", "black", flag = true, Cat.State.Normal),
        Cat("garfield", "orange", flag = true, Cat.State.Good),
        Cat("creme puff", "grey", flag = true, Cat.State.VeryGood)
      )
      await(db.run((for {
        oldCats <- cats.result
        _ <- cats.delete
        _ <- cats ++= testKitties
        _ <- cats.result.map(_ must equal(testKitties))
        _ <- cats.delete
        _ <- cats ++= oldCats
      } yield ()).transactionally))
    }
  }

}
