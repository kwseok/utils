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
    parallelExecution in Test := false,
    fork in Test := true,
    resolvers ++= Dependencies.resolvers,
    libraryDependencies ++= Dependencies.core,
    libraryDependencies ++= Dependencies.compilerPlugins
  )
}

object RootSettings extends AutoPlugin {
  override def requires: Plugins = BintrayPlugin

  override def projectSettings = Seq(
    publishArtifact := false,
    publish := (),
    publishTo := None,
    bintrayRelease := (),
    bintrayUnpublish := ()
  )
}

object LibrarySettings extends AutoPlugin {
  override def requires: Plugins = BintrayPlugin

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
  val resolvers: Seq[Resolver] = DefaultOptions.resolvers(snapshot = true) ++ Seq(
    Resolver.sbtPluginRepo("releases"),
    Resolver.bintrayRepo("scalaz", "releases"),
    Resolver.jcenterRepo
  )

  val core: Seq[ModuleID] = Seq(
    Libraries.scalaXml,
    Libraries.jodaTime,
    Libraries.jodaConvert,
    Libraries.cats,
    Libraries.shapeless,
    Libraries.findbugs
  )

  val compilerPlugins: Seq[ModuleID] = Seq(
    Libraries.paradise,
    Libraries.kindProjector
  ).map(compilerPlugin)

  val playUtil: Seq[ModuleID] = Seq(
    Libraries.play,
    Libraries.playCache,
    Libraries.playWs,
    Libraries.playTest % Test,
    Libraries.scalatestplus_play % Test
  )

  val slickExt: Seq[ModuleID] = Seq(
    Libraries.slick,
    Libraries.logbackClassic,
    Libraries.scalatest % Test
  )

  val slickPg: Seq[ModuleID] = Seq(
    Libraries.slick,
    Libraries.slickPg,
    Libraries.playJson,
    Libraries.logbackClassic,
    Libraries.h2 % Test,
    Libraries.scalatest % Test,
    Libraries.postgresql % Provided
  )

  val scalaUtil: Seq[ModuleID] = Seq(
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
}

object Versions {
  val scalaVersions = Seq("2.11.11", "2.12.3")

  val scalaXml = "1.0.6"

  val paradise      = "2.1.1"
  val kindProjector = "0.9.4"

  val cats      = "0.9.0"
  val shapeless = "2.3.2"

  val jodaTime    = "2.9.9"
  val jodaConvert = "1.9.2"

  val play: String  = _root_.play.core.PlayVersion.current

  val slick   = "3.2.1"
  val slickPg = "0.15.3"

  val h2         = "1.4.196"
  val postgresql = "42.1.4"

  val guava       = "23.1-jre"
  val jsoup       = "1.10.3"
  val ficus       = "1.4.2"
  val scalaGuice  = "4.1.0"
  val htmlcleaner = "2.21"

  val commonsIO    = "2.5"
  val commonsCodec = "1.10"
  val commonsLang3 = "3.6"

  val logbackClassic = "1.2.3"

  val scalatest          = "3.0.4"
  val scalatestplus_play = "3.1.2"

  val findbugs = "3.0.2"
}

object Libraries {
  val scalaXml = "org.scala-lang.modules" %% "scala-xml" % Versions.scalaXml

  val paradise      = "org.scalamacros" % "paradise" % Versions.paradise cross CrossVersion.full
  val kindProjector = "org.spire-math" %% "kind-projector" % Versions.kindProjector

  val cats      = "org.typelevel" %% "cats" % Versions.cats
  val shapeless = "com.chuusai" %% "shapeless" % Versions.shapeless

  val jodaTime    = "joda-time" % "joda-time" % Versions.jodaTime
  val jodaConvert = "org.joda" % "joda-convert" % Versions.jodaConvert

  val play        = "com.typesafe.play" %% "play" % Versions.play
  val playCache   = "com.typesafe.play" %% "play-cache" % Versions.play
  val playJdbcApi = "com.typesafe.play" %% "play-jdbc-api" % Versions.play
  val playJdbc    = "com.typesafe.play" %% "play-jdbc" % Versions.play
  val playJson    = "com.typesafe.play" %% "play-json" % Versions.play
  val playTest    = "com.typesafe.play" %% "play-test" % Versions.play
  val playWs      = "com.typesafe.play" %% "play-ws" % Versions.play

  val slick   = "com.typesafe.slick" %% "slick" % Versions.slick
  val slickPg = "com.github.tminglei" %% "slick-pg" % Versions.slickPg

  val h2         = "com.h2database" % "h2" % Versions.h2
  val postgresql = "org.postgresql" % "postgresql" % Versions.postgresql

  val guava       = "com.google.guava" % "guava" % Versions.guava
  val jsoup       = "org.jsoup" % "jsoup" % Versions.jsoup
  val ficus       = "com.iheart" %% "ficus" % Versions.ficus
  val scalaGuice  = "net.codingwell" %% "scala-guice" % Versions.scalaGuice
  val htmlcleaner = "net.sourceforge.htmlcleaner" % "htmlcleaner" % Versions.htmlcleaner

  val commonsIO    = "commons-io" % "commons-io" % Versions.commonsIO
  val commonsCodec = "commons-codec" % "commons-codec" % Versions.commonsCodec
  val commonsLang3 = "org.apache.commons" % "commons-lang3" % Versions.commonsLang3

  val logbackClassic = "ch.qos.logback" % "logback-classic" % Versions.logbackClassic

  val scalatest          = "org.scalatest" %% "scalatest" % Versions.scalatest
  val scalatestplus_play = "org.scalatestplus.play" %% "scalatestplus-play" % Versions.scalatestplus_play

  val findbugs = "com.google.code.findbugs" % "jsr305" % Versions.findbugs
}
