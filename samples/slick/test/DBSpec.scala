import com.github.stonexx.play.db.slick.DB
import org.scalatestplus.play._
import play.api.db.DBApi
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.test._
import play.api.test.Helpers._

/**
 * test the kitty cat database
 */
class DBSpec extends PlaySpec with OneAppPerTest {

  "DB" should {
    "work as expected" in {
      import models._
      import slick.Tables._, profile.api._

      val cats = TableQuery[Cats]

      val testKitties = Seq(
        Cat("kit", "black", flag = true, Cat.State.Normal),
        Cat("garfield", "orange", flag = true, Cat.State.Good),
        Cat("creme puff", "grey", flag = true, Cat.State.VeryGood)
      )
      await(DB.run((for {
        oldCats <- cats.result
        _ <- cats.delete
        _ <- cats ++= testKitties
        _ <- cats.result.map(_ must equal(testKitties))
        _ <- cats.delete
        _ <- cats ++= oldCats
      } yield ()).transactionally))
    }

    "use the correct db settings when specified" in {
      app.injector.instanceOf[DBApi].database("specific").withConnection { implicit conn =>
        conn.getMetaData.getURL must equal("jdbc:h2:mem:veryspecialindeed")
      }
    }

    "use the default db settings when no other possible options are available" in {
      app.injector.instanceOf[DBApi].database("default").withConnection { implicit conn =>
        conn.getMetaData.getURL must equal("jdbc:h2:mem:play")
      }
    }
  }

}
