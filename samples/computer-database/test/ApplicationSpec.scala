import org.scalatestplus.play._
import play.api.Application
import play.api.test._
import play.api.test.Helpers._
import play.api.test.CSRFTokenHelper._

class ApplicationSpec extends PlaySpec with guice.GuiceOneAppPerTest {

  // -- Date helpers

  def dateIs(date: java.util.Date, str: String): Boolean =
    new java.text.SimpleDateFormat("yyyy-MM-dd").format(date) == str

  // -j-

  "Application" should {
    import models.Computer.forms._

    def applicationController(implicit app: Application) = {
      val app2ApplicationController = Application.instanceCache[controllers.Application]
      app2ApplicationController(app)
    }

    "redirect to the computer list on /" in {
      val result = applicationController.index(FakeRequest())

      status(result) must equal(SEE_OTHER)
      redirectLocation(result) must contain("/computers")
    }

    "list computers on the the first page" in {
      val result = applicationController.list(1, "", Some(Sorts.Name.asc))(FakeRequest())

      status(result) must equal(OK)
      contentAsString(result) must include("574 computers found")
    }

    "filter computer by name" in {
      val result = applicationController.list(1, "Apple", Some(Sorts.Name.asc))(FakeRequest())

      status(result) must equal(OK)
      contentAsString(result) must include("13 computers found")

    }

    "create new computer" in {
      val badResult = applicationController.save(FakeRequest().withCSRFToken)

      status(badResult) must equal(BAD_REQUEST)

      val badDateFormat = applicationController.save(
        FakeRequest().withFormUrlEncodedBody(
          "name" -> "FooBar",
          "introduced" -> "badbadbad",
          "company" -> "1"
        ).withCSRFToken
      )

      status(badDateFormat) must equal(BAD_REQUEST)
      contentAsString(badDateFormat) must include("""<option value="1" selected="selected">Apple Inc.</option>""")
      contentAsString(badDateFormat) must include("""<input type="text" id="introduced" name="introduced" value="badbadbad" />""")
      contentAsString(badDateFormat) must include("""<input type="text" id="name" name="name" value="FooBar" />""")

      val result = applicationController.save(
        FakeRequest().withFormUrlEncodedBody(
          "name" -> "FooBar",
          "introduced" -> "2011-12-24",
          "company" -> "1"
        ).withCSRFToken
      )

      status(result) must equal(SEE_OTHER)
      redirectLocation(result) must contain("/computers")
      flash(result).get("success") must contain("Computer FooBar has been created")

      val list = applicationController.list(1, "FooBar", Some(Sorts.Name.asc))(FakeRequest())

      status(list) must equal(OK)
      contentAsString(list) must include("One computer found")
    }
  }

}