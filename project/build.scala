import bintray.BintrayPlugin
import bintray.BintrayKeys._
import sbt._
import sbt.Keys._
import sbt.plugins._

object Build extends AutoPlugin {
  override def requires: Plugins = JvmPlugin
  override def trigger: PluginTrigger = allRequirements

  override def projectSettings = Seq(
    organization := "com.github.stonexx",
    scalaVersion := Versions.scalaVersions.min,
    crossScalaVersions := Versions.scalaVersions,
    scalacOptions ++= Seq(
      "-encoding", "utf8",
      "-deprecation",
      "-unchecked",
      "-feature",
      "-Xcheckinit",
      "-language:implicitConversions",
      "-language:reflectiveCalls",
      "-language:higherKinds",
      "-language:postfixOps"
    ),
    javacOptions in Compile ++= Seq("-encoding", "utf8", "-g"),
    resolvers ++= Dependencies.resolvers,
    parallelExecution in Test := false,
    fork in Test := true
  )
}

object RootSettings extends AutoPlugin {
  override def requires = BintrayPlugin

  override def projectSettings = Seq(
    publishArtifact := false,
    publish := (),
    publishTo := None,
    bintrayRelease := (),
    bintrayUnpublish := ()
  )
}

object LibrarySettings extends AutoPlugin {
  override def requires = BintrayPlugin

  override def projectSettings = Seq(
    credentials += Credentials(Path.userHome / ".ivy2" / ".credentials-corezetta"),
    publishTo := {
      if (isSnapshot.value)
        Some("corezetta-nexus-snapshots" at "http://nexus.corezetta.com/content/repositories/snapshots")
      //Some("OJO" at "https://oss.jfrog.org/oss-snapshot-local/")
      else
        (publishTo in bintray).value
    },
    bintrayOrganization := Some("stonexx"),
    bintrayReleaseOnPublish := !isSnapshot.value,
    publishMavenStyle := true,
    pomIncludeRepository := { _ => false },
    pomExtra := {
      <url>https://github.com/stonexx/scala</url>
        <scm>
          <url>git://github.com/stonexx/scala.git</url>
          <connection>scm:git:git://github.com/stonexx/scala.git</connection>
        </scm>
        <developers>
          <developer>
            <id>stonexx</id>
            <name>Kiwon Seok</name>
          </developer>
        </developers>
    }
  )
}

object Dependencies {
  val compilerPlugins: Seq[ModuleID] = Seq(
    Libraries.paradise,
    Libraries.kindProjector
  ).map(compilerPlugin)

  val common: Seq[ModuleID] = Seq(
    Libraries.scalaXml,
    Libraries.jodaTime,
    Libraries.jodaConvert,
    Libraries.cats,
    Libraries.shapeless,
    Libraries.findbugs
  ) ++ compilerPlugins

  val play: Seq[ModuleID] = common ++ Seq(
    Libraries.play,
    Libraries.playCache,
    Libraries.playWs,
    Libraries.playTest % Test,
    Libraries.scalatestplus_play % Test
  )

  val playSlick: Seq[ModuleID] = play ++ Seq(
    Libraries.slick,
    Libraries.slickHikariCP,
    Libraries.playJdbcApi,
    Libraries.h2 % Test
  )

  val playSlickJdbcAdapter: Seq[ModuleID] = play ++ Seq(
    Libraries.h2 % Test
  )

  val playUtil: Seq[ModuleID] = play

  val slickExt: Seq[ModuleID] = common ++ Seq(
    Libraries.slick,
    Libraries.logbackClassic,
    Libraries.scalatest % Test
  )

  val slickPg: Seq[ModuleID] = common ++ Seq(
    Libraries.slick,
    Libraries.slickPg,
    Libraries.playJson,
    Libraries.logbackClassic,
    Libraries.h2 % Test,
    Libraries.scalatest % Test,
    Libraries.postgresql % Provided
  )

  val scalaUtil: Seq[ModuleID] = common ++ Seq(
    Libraries.guava,
    Libraries.jsoup,
    Libraries.ficus,
    Libraries.scalaGuice,
    Libraries.htmlcleaner,
    Libraries.commonsIO,
    Libraries.commonsCodec,
    Libraries.commonsLang3,
    Libraries.logbackClassic,
    Libraries.scalatest % Test
  )

  val resolvers: Seq[Resolver] = DefaultOptions.resolvers(snapshot = true) ++ Seq(
    Resolver.sbtPluginRepo("releases"),
    Resolver.bintrayRepo("scalaz", "releases"),
    Resolver.jcenterRepo
  )
}

object Versions {
  val scalaVersions = Seq("2.11.11", "2.12.2")

  val scalaXml   = "1.0.6"
  val scalaAsync = "0.9.6"

  val paradise      = "2.1.1"
  val kindProjector = "0.9.4"

  val cats      = "0.9.0"
  val shapeless = "2.3.2"

  val jodaTime    = "2.9.9"
  val jodaConvert = "1.8.2"

  val play: String = _root_.play.core.PlayVersion.current
  val playJson     = "2.6.2"

  val slick   = "3.2.0"
  val slickPg = "0.15.1"

  val h2         = "1.4.196"
  val postgresql = "42.1.1"

  val guava       = "22.0"
  val jsoup       = "1.10.3"
  val ficus       = "1.4.1"
  val scalaGuice  = "4.1.0"
  val htmlcleaner = "2.21"

  val commonsIO    = "2.5"
  val commonsCodec = "1.10"
  val commonsLang3 = "3.6"

  val logbackClassic = "1.2.3"

  val scalatest          = "3.0.3"
  val scalatestplus_play = "3.0.0"

  val findbugs = "3.0.2"
}

object Libraries {
  val scalaXml  : ModuleID = "org.scala-lang.modules" %% "scala-xml" % Versions.scalaXml
  val scalaAsync: ModuleID = "org.scala-lang.modules" %% "scala-async" % Versions.scalaAsync

  val paradise     : ModuleID = "org.scalamacros" % "paradise" % Versions.paradise cross CrossVersion.full
  val kindProjector: ModuleID = "org.spire-math" %% "kind-projector" % Versions.kindProjector

  val cats     : ModuleID = "org.typelevel" %% "cats" % Versions.cats
  val shapeless: ModuleID = "com.chuusai" %% "shapeless" % Versions.shapeless

  val jodaTime   : ModuleID = "joda-time" % "joda-time" % Versions.jodaTime
  val jodaConvert: ModuleID = "org.joda" % "joda-convert" % Versions.jodaConvert

  val play       : ModuleID = "com.typesafe.play" %% "play" % Versions.play
  val playCache  : ModuleID = "com.typesafe.play" %% "play-cache" % Versions.play
  val playJdbcApi: ModuleID = "com.typesafe.play" %% "play-jdbc-api" % Versions.play
  val playJdbc   : ModuleID = "com.typesafe.play" %% "play-jdbc" % Versions.play
  val playJson   : ModuleID = "com.typesafe.play" %% "play-json" % Versions.playJson
  val playTest   : ModuleID = "com.typesafe.play" %% "play-test" % Versions.play
  val playWs     : ModuleID = "com.typesafe.play" %% "play-ws" % Versions.play

  val slick        : ModuleID = "com.typesafe.slick" %% "slick" % Versions.slick
  val slickHikariCP: ModuleID = "com.typesafe.slick" %% "slick-hikaricp" % Versions.slick
  val slickPg      : ModuleID = "com.github.tminglei" %% "slick-pg" % Versions.slickPg

  val h2        : ModuleID = "com.h2database" % "h2" % Versions.h2
  val postgresql: ModuleID = "org.postgresql" % "postgresql" % Versions.postgresql

  val guava      : ModuleID = "com.google.guava" % "guava" % Versions.guava
  val jsoup      : ModuleID = "org.jsoup" % "jsoup" % Versions.jsoup
  val ficus      : ModuleID = "com.iheart" %% "ficus" % Versions.ficus
  val scalaGuice : ModuleID = "net.codingwell" %% "scala-guice" % Versions.scalaGuice
  val htmlcleaner: ModuleID = "net.sourceforge.htmlcleaner" % "htmlcleaner" % Versions.htmlcleaner

  val commonsIO   : ModuleID = "commons-io" % "commons-io" % Versions.commonsIO
  val commonsCodec: ModuleID = "commons-codec" % "commons-codec" % Versions.commonsCodec
  val commonsLang3: ModuleID = "org.apache.commons" % "commons-lang3" % Versions.commonsLang3

  val logbackClassic: ModuleID = "ch.qos.logback" % "logback-classic" % Versions.logbackClassic

  val scalatest         : ModuleID = "org.scalatest" %% "scalatest" % Versions.scalatest
  val scalatestplus_play: ModuleID = "org.scalatestplus.play" %% "scalatestplus-play" % Versions.scalatestplus_play

  val findbugs: ModuleID = "com.google.code.findbugs" % "jsr305" % Versions.findbugs
}
