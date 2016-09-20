lazy val root = project.in(file(".")).enablePlugins(Root).aggregate(
  playAnorm,
  playSlick,
  playSlickJdbcAdapter,
  playUtil,
  slickExt,
  slickPg,
  scalaUtil
)

lazy val playAnorm = Project("play-anorm", file("src/play-anorm"))
  .enablePlugins(Lib)
  .settings(libraryDependencies ++= Dependencies.playAnorm)
  .dependsOn(playUtil)

lazy val playSlick = Project("play-slick", file("src/play-slick"))
  .enablePlugins(Lib)
  .settings(libraryDependencies ++= Dependencies.playSlick)
  .dependsOn(playUtil)

lazy val playSlickJdbcAdapter = Project("play-slick-jdbc-adapter", file("src/play-slick-jdbc-adapter"))
  .enablePlugins(Lib)
  .settings(libraryDependencies ++= Dependencies.playSlick)
  .dependsOn(playSlick)

lazy val playUtil = Project("play-util", file("src/play-util"))
  .enablePlugins(Lib)
  .settings(libraryDependencies ++= Dependencies.playUtil)
  .dependsOn(scalaUtil)

lazy val slickExt = Project("slick-ext", file("src/slick-ext"))
  .enablePlugins(Lib)
  .settings(libraryDependencies ++= Dependencies.slickExt)
  .dependsOn(scalaUtil)

lazy val slickPg = Project("slick-pg", file("src/slick-pg"))
  .enablePlugins(Lib)
  .settings(libraryDependencies ++= Dependencies.slickPg)
  .dependsOn(scalaUtil, slickExt)

lazy val scalaUtil = Project("scala-util", file("src/scala-util"))
  .enablePlugins(Lib)
  .settings(libraryDependencies ++= Dependencies.scalaUtil)

lazy val samples = project.in(file("samples")).aggregate(
  computerDatabaseSample,
  slickSample
)

def sampleProject(name: String) = Project(s"$name-sample", file("samples") / name)
  .settings(
    concurrentRestrictions in Global += Tags.limit(Tags.Test, 1),
    libraryDependencies += Library.h2,
    libraryDependencies += Library.scalatestplus_play % Test
  )
  .enablePlugins(PlayScala, DependencyGraphPlugin)

lazy val computerDatabaseSample = sampleProject("computer-database")
  .dependsOn(
    playSlick,
    playSlickJdbcAdapter,
    playUtil,
    slickExt
  )

lazy val slickSample = sampleProject("slick")
  .settings(libraryDependencies += Library.postgresql)
  .dependsOn(
    playSlick,
    playSlickJdbcAdapter,
    playUtil,
    scalaUtil,
    slickExt,
    slickPg
  )
