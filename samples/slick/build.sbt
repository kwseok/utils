name := "slick-sample"

PlayKeys.playDefaultPort := 9001

routesImport ++= Seq(
  "com.github.stonexx.play.mvc.binders.joda._",
  "com.github.stonexx.play.mvc.binders.option._"
)

addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full)
addCompilerPlugin("org.spire-math" %% "kind-projector" % "0.8.0")

libraryDependencies ++= Seq(
  "org.scala-lang.modules" %% "scala-async" % "0.9.5",
  "org.scala-lang.modules" %% "scala-xml" % "1.0.5"
)

libraryDependencies ++= Seq(
  cache,
  evolutions,
  filters,
  ws,
  component("play-streams")
)

libraryDependencies ++= {
  val akkaVersion = "2.4.9"
  val camelVersion = "2.17.3"
  Seq(
    "com.typesafe.akka" %% "akka-actor" % akkaVersion,
    "com.typesafe.akka" %% "akka-contrib" % akkaVersion,
    "com.typesafe.akka" %% "akka-slf4j" % akkaVersion,
    "com.typesafe.akka" %% "akka-camel" % akkaVersion,
    "com.typesafe.akka" %% "akka-testkit" % akkaVersion % Test,
    "org.apache.camel" % "camel-jetty" % camelVersion,
    "org.apache.camel" % "camel-quartz" % camelVersion
  )
}

libraryDependencies ++= Seq(
  "com.github.tminglei" %% "slick-pg_joda-time" % Version.slickPg,
  "com.github.tminglei" %% "slick-pg_play-json" % Version.slickPg
)

libraryDependencies ++= Seq(
  "org.apache.poi" % "poi-ooxml" % "3.14",
  "eu.bitwalker" % "UserAgentUtils" % "1.20"
)

LessKeys.compress := true
includeFilter in (Assets, LessKeys.less) := "*.less"
excludeFilter in (Assets, LessKeys.less) := "_*.less"

RjsKeys.mainConfig := "require-config"
RjsKeys.paths += ("routes" -> ("/routes.js" -> "empty:"))
RjsKeys.optimize := "none"

excludeFilter in uglify ~= (_ || "*.min.js")

HtmlMinifierKeys.minifyCSS := true
HtmlMinifierKeys.minifyJS := true

pipelineStages := Seq(rjs, uglify, htmlMinifier, digest, gzip)

initialCommands := """
                     |implicit val _app = {
                     |  import play.api._
                     |  val env = Environment(new java.io.File("."), this.getClass.getClassLoader, Mode.Dev)
                     |  val context = ApplicationLoader.createContext(env, initialSettings = Map(
                     |    "play.evolutions.autoApply" -> "true"
                     |  ))
                     |  val loader = ApplicationLoader(context)
                     |  val app = loader.load(context)
                     |  Play.start(app)
                     |  app
                     |}
                   """.stripMargin
cleanupCommands := "play.api.Play.stop(_app)"
