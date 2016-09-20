name := "computer-database-sample"

PlayKeys.playDefaultPort := 9002

PlayKeys.playOmnidoc := false

routesImport ++= Seq(
  "com.github.stonexx.play.mvc.binders.joda._",
  "com.github.stonexx.play.mvc.binders.option._"
)

libraryDependencies ++= Seq(
  cache,
  evolutions,
  filters,
  ws,
  component("play-streams")
)

initialCommands := """
                     |implicit val _app = {
                     |  import play.api._
                     |  val env = Environment(new java.io.File("."), this.getClass.getClassLoader, Mode.Dev)
                     |  val context = ApplicationLoader.createContext(env, initialSettings = Map(
                     |    "play.evolutions.autoApply" -> "true"
                     |  ))
                     |  val loader = ApplicationLoader(context)
                     |  loader.load(context)
                     |}
                     |play.api.Play.start(_app)
                   """.stripMargin
cleanupCommands := "play.api.Play.stop(_app)"
