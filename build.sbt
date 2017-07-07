lazy val root = project.in(file(".")).enablePlugins(RootSettings).aggregate(
  playSlick,
  playSlickJdbcAdapter,
  playUtil,
  slickExt,
  slickPg,
  scalaUtil
)

lazy val playSlick = Project("play-slick", file("src/play-slick"))
  .enablePlugins(LibrarySettings)
  .settings(libraryDependencies ++= Dependencies.playSlick)
  .dependsOn(playUtil)

lazy val playSlickJdbcAdapter = Project("play-slick-jdbc-adapter", file("src/play-slick-jdbc-adapter"))
  .enablePlugins(LibrarySettings)
  .settings(libraryDependencies ++= Dependencies.playSlick)
  .dependsOn(playSlick)

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

lazy val scalaUtil = Project("scala-util", file("src/scala-util"))
  .enablePlugins(LibrarySettings)
  .settings(libraryDependencies ++= Dependencies.scalaUtil)

lazy val samples = project.in(file("samples")).aggregate(
  computerDatabaseSample,
  slickSample
)

def sampleProject(name: String) = Project(s"$name-sample", file("samples") / name)
  .settings(
    concurrentRestrictions in Global += Tags.limit(Tags.Test, 1),
    libraryDependencies += Libraries.h2,
    libraryDependencies += Libraries.scalatestplus_play % Test,
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
  .enablePlugins(PlayScala, DependencyGraphPlugin)

lazy val computerDatabaseSample = sampleProject("computer-database")
  .settings(
    PlayKeys.playDefaultPort := 9001,
    PlayKeys.playOmnidoc := false,
    routesImport ++= Seq(
      "com.github.stonexx.play.mvc.binders.option._"
    ),
    libraryDependencies ++= Seq(
      ehcache,
      guice,
      evolutions,
      filters,
      ws,
      component("play-streams"),
      Libraries.playJson
    )
  )
  .dependsOn(
    playSlick,
    playSlickJdbcAdapter,
    playUtil,
    slickExt
  )

lazy val slickSample = sampleProject("slick")
  .settings(
    PlayKeys.playDefaultPort := 9002,
    routesImport ++= Seq(
      "com.github.stonexx.play.mvc.binders.option._"
    ),
    libraryDependencies ++= Dependencies.compilerPlugins,
    libraryDependencies ++= Seq(
      Libraries.scalaXml,
      Libraries.scalaAsync
    ),
    libraryDependencies ++= Seq(
      ehcache,
      guice,
      evolutions,
      filters,
      ws,
      component("play-streams")
    ),
    libraryDependencies += Libraries.postgresql,
    libraryDependencies ++= {
      val akkaVersion = "2.5.3"
      val camelVersion = "2.19.1"
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
      "org.apache.poi" % "poi-ooxml" % "3.16"
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
    // Html minifier
    HtmlMinifierKeys.minifyCSS := true,
    HtmlMinifierKeys.minifyJS := true,
    // Pipeline stages
    pipelineStages := Seq(rjs, uglify, htmlMinifier, digest, gzip)
  )
  .dependsOn(
    playSlick,
    playSlickJdbcAdapter,
    playUtil,
    scalaUtil,
    slickExt,
    slickPg
  )
