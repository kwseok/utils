lazy val root = project.in(file(".")).enablePlugins(RootSettings).aggregate(
  scalaUtil,
  playUtil,
  slickExt,
  slickPg
)

lazy val scalaUtil = Project("scala-util", file("src/scala-util"))
  .enablePlugins(LibrarySettings)
  .settings(libraryDependencies ++= Dependencies.scalaUtil)

lazy val playUtil = Project("play-util", file("src/play-util"))
  .enablePlugins(LibrarySettings)
  .settings(libraryDependencies ++= Dependencies.playUtil)
  .dependsOn(scalaUtil)

lazy val slickExt = Project("slick-ext", file("src/slick-ext"))
  .enablePlugins(LibrarySettings)
  .settings(libraryDependencies ++= Dependencies.slickExt)
  .dependsOn(scalaUtil)

lazy val slickPg = Project("slick-pg", file("src/slick-pg"))
  .enablePlugins(LibrarySettings)
  .settings(libraryDependencies ++= Dependencies.slickPg)
  .dependsOn(scalaUtil, slickExt)

lazy val samples = project.in(file("samples")).aggregate(
  computerDatabaseSample,
  slickSample
)

def sampleProject(name: String) = Project(s"$name-sample", file("samples") / name)
  .enablePlugins(DependencyGraphPlugin)
  .settings(
    concurrentRestrictions in Global += Tags.limit(Tags.Test, 1),
    libraryDependencies += Libraries.scalatestplus_play % Test
  )
  .dependsOn(scalaUtil)

def playSampleProject(name: String, port: Int) = sampleProject(name)
  .enablePlugins(PlayScala)
  .settings(
    PlayKeys.playDefaultPort := port,
    PlayKeys.playOmnidoc := false,
    routesImport ++= Seq(
      "com.github.stonexx.play.mvc.binders.option._"
    ),
    libraryDependencies ++= Seq(
      ehcache,
      evolutions,
      filters,
      guice,
      ws
    ),
    libraryDependencies += Libraries.playJson,
    libraryDependencies += Libraries.h2,
    initialCommands :=
      """
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
      """.stripMargin,
    cleanupCommands := "play.api.Play.stop(_app)"
  )
  .dependsOn(playUtil)

def playSlickSampleProject(name: String, port: Int) = playSampleProject(name, port).settings(
  libraryDependencies ++= Seq(
    "com.typesafe.play" %% "play-slick" % "3.0.1",
    "com.typesafe.play" %% "play-slick-evolutions" % "3.0.1"
  )
).dependsOn(slickExt)

lazy val computerDatabaseSample = playSlickSampleProject(name = "computer-database", port = 9001)

lazy val slickSample = playSlickSampleProject(name = "slick", port = 9002)
  .settings(
    libraryDependencies += Libraries.postgresql,
    libraryDependencies ++= {
      val akkaVersion = "2.5.6"
      val camelVersion = "2.19.3"
      Seq(
        "com.typesafe.akka" %% "akka-actor" % akkaVersion,
        "com.typesafe.akka" %% "akka-contrib" % akkaVersion,
        "com.typesafe.akka" %% "akka-slf4j" % akkaVersion,
        "com.typesafe.akka" %% "akka-camel" % akkaVersion,
        "com.typesafe.akka" %% "akka-testkit" % akkaVersion % Test,
        "org.apache.camel" % "camel-jetty" % camelVersion,
        "org.apache.camel" % "camel-quartz" % camelVersion
      )
    },
    libraryDependencies ++= Seq(
      "com.github.tminglei" %% "slick-pg_play-json" % Versions.slickPg,
      "org.apache.poi" % "poi-ooxml" % "3.17"
    ),
    // Less
    LessKeys.compress := true,
    includeFilter in (Assets, LessKeys.less) := "*.less",
    excludeFilter in (Assets, LessKeys.less) := "_*.less",
    // Rjs
    RjsKeys.mainConfig := "require-config",
    RjsKeys.paths += ("routes" -> ("/routes.js" -> "empty:")),
    RjsKeys.optimize := "none",
    // Uglify
    excludeFilter in uglify ~= (_ || "*.min.js"),
    // ReactJS
    ReactJsKeys.harmony := true,
    ReactJsKeys.stripTypes := true,
    ReactJsKeys.sourceMapInline := true,
    ReactJsKeys.es6module := true,
    // Html minifier
    HtmlMinifierKeys.minifyCSS := true,
    HtmlMinifierKeys.minifyJS := true,
    // Pipeline stages
    pipelineStages := Seq(rjs, uglify, htmlMinifier, digest, gzip)
  )
  .dependsOn(slickPg)
