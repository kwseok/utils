import org.fluentlenium.core.filter.FilterConstructor.withText
import org.scalatestplus.play._
import play.api.test._
import play.api.test.Helpers._

class IntegrationSpec extends PlaySpec with OneServerPerTest with OneBrowserPerTest with HtmlUnitFactory {

  "Application" should {

    "work from within a browser" in {
      val port = 3335
      running(TestServer(port), HTMLUNIT) { browser =>
        browser.goTo("http://localhost:" + port)

        browser.$("header h1").first.getText must equal("Play sample application — Computer database")
        browser.$("section h1").first.getText must equal("574 computers found")

        browser.$("#pagination li.current").first.getText must equal("Displaying 1 to 10 of 574")

        browser.$("#pagination li.next a").click()

        browser.$("#pagination li.current").first.getText must equal("Displaying 11 to 20 of 574")
        browser.$("#searchbox").text("Apple")
        browser.$("#searchsubmit").click()

        browser.$("section h1").first.getText must equal("13 computers found")
        browser.$("section table tbody tr td a", withText("Apple II")).click()

        browser.$("section h1").first.getText must equal("Edit computer")

        browser.$("#discontinued").text("xxx")
        browser.$("input.primary").click()

        browser.$("div.error").size must equal(1)
        browser.$("div.error label").first.getText must equal("Discontinued date")

        browser.$("#discontinued").text("")
        browser.$("input.primary").click()

        browser.$("section h1").first.getText must equal("574 computers found")
        browser.$(".alert-message").first.getText must equal("Done! Computer Apple II has been updated")

        browser.$("#searchbox").text("Apple")
        browser.$("#searchsubmit").click()

        browser.$("a", withText("Apple II")).click()
        browser.$("input.danger").click()

        browser.$("section h1").first.getText must equal("573 computers found")
        browser.$(".alert-message").first.getText must equal("Done! Computer has been deleted")

        browser.$("#searchbox").text("Apple")
        browser.$("#searchsubmit").click()

        browser.$("section h1").first.getText must equal("12 computers found")
      }
    }

  }

}
