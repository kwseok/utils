import dao.ComputerDao
import org.scalatestplus.play._
import play.api.Application

import scala.concurrent.Await
import scala.concurrent.duration.Duration

class ModelSpec extends PlaySpec with guice.GuiceOneAppPerTest {

  import models._

  // -- Date helpers

  def dateStr(date: java.util.Date): String = new java.text.SimpleDateFormat("yyyy-MM-dd").format(date)

  // --

  "Computer model" should {

    def computerDao(implicit app: Application) = {
      val app2ComputersDAO = Application.instanceCache[ComputerDao]
      app2ComputersDAO(app)
    }

    "be retrieved by id" in {
      val macintosh = Await.result(computerDao.getById(21l), Duration.Inf)
      macintosh.name must equal("Macintosh")
      macintosh.introduced.map(dateStr) must contain("1984-01-24")
    }

    "be listed along its companies" in {
      val computers = Await.result(computerDao.list(), Duration.Inf)
      computers.total must equal(574)
      computers.items must have length 10
    }

    "be updated if needed" in {
      Await.result(computerDao.update(21l, Computer(
        id = Some(21),
        name = "The Macintosh",
        introduced = None,
        discontinued = None,
        companyId = Some(1)
      )), Duration.Inf)

      val macintosh = Await.result(computerDao.getById(21l), Duration.Inf)
      macintosh.name must equal("The Macintosh")
      macintosh.introduced must equal(None)
    }

  }
}