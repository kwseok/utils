import bintray.BintrayPlugin
import bintray.BintrayKeys._
import sbt._
import sbt.Keys._
import sbt.plugins._

object CommonSettings extends AutoPlugin {
  override def requires: Plugins = JvmPlugin
  override def trigger: PluginTrigger = allRequirements

  override def projectSettings = Seq(
    organization := "com.github.stonexx",
    scalaVersion := Version.scala,
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

object Root extends AutoPlugin {
  override def requires = BintrayPlugin

  override def projectSettings = Seq(
    publishArtifact := false,
    publish := (),
    publishTo := None,
    bintrayRelease := (),
    bintrayUnpublish := ()
  )
}

object Lib extends AutoPlugin {
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
    Library.macroParadise,
    Library.kindProjector
  ).map(compilerPlugin)

  val common: Seq[ModuleID] = Seq(
    Library.scalaCompiler,
    Library.scalaReflect,
    Library.scalaXml,
    Library.jodaTime,
    Library.jodaConvert,
    Library.cats,
    Library.shapeless,
    Library.findbugs
  ) ++ compilerPlugins

  val play: Seq[ModuleID] = common ++ Seq(
    Library.play,
    Library.playCache,
    Library.playWs,
    Library.playTest % Test,
    Library.scalatestplus_play % Test
  )

  val playAnorm: Seq[ModuleID] = play ++ Seq(
    Library.anorm,
    Library.playJdbc,
    Library.h2 % Test,
    Library.postgresql % Provided
  )

  val playSlick: Seq[ModuleID] = play ++ Seq(
    Library.slick,
    Library.slickHikariCP,
    Library.playJdbcApi,
    Library.h2 % Test
  )

  val playSlickJdbcAdapter: Seq[ModuleID] = play ++ Seq(
    Library.h2 % Test
  )

  val playUtil: Seq[ModuleID] = play

  val slickExt: Seq[ModuleID] = common ++ Seq(
    Library.slick,
    Library.logbackClassic,
    Library.scalatest % Test
  )

  val slickPg: Seq[ModuleID] = common ++ Seq(
    Library.slick,
    Library.slickPg,
    Library.playJson,
    Library.logbackClassic,
    Library.h2 % Test,
    Library.scalatest % Test,
    Library.postgresql % Provided
  )

  val scalaUtil: Seq[ModuleID] = common ++ Seq(
    Library.guava,
    Library.jsoup,
    Library.ficus,
    Library.scalaGuice,
    Library.htmlcleaner,
    Library.commonsIO,
    Library.commonsCodec,
    Library.commonsLang3,
    Library.logbackClassic,
    Library.scalatest % Test
  )

  val resolvers: Seq[Resolver] = DefaultOptions.resolvers(snapshot = true) ++ Seq(
    Resolver.sbtPluginRepo("releases"),
    Resolver.bintrayRepo("scalaz", "releases"),
    Resolver.jcenterRepo
  )
}

object Version {
  val scala    = "2.11.8"
  val scalaXml = "1.0.5"

  val macroParadise = "2.1.0"
  val kindProjector = "0.9.0"

  val cats      = "0.7.2"
  val shapeless = "2.3.2"

  val jodaTime    = "2.9.4"
  val jodaConvert = "1.8.1"

  val play: String = _root_.play.core.PlayVersion.current

  val anorm = "2.5.2"

  val slick   = "3.2.0-M1"
  val slickPg = "0.15.0-M2"

  val h2         = "1.4.192"
  val postgresql = "9.4-1201-jdbc41"

  val guava       = "19.0"
  val jsoup       = "1.9.2"
  val ficus       = "1.3.0"
  val scalaGuice  = "4.1.0"
  val htmlcleaner = "2.16"

  val commonsIO    = "2.5"
  val commonsCodec = "1.10"
  val commonsLang3 = "3.4"

  val logbackClassic = "1.1.7"

  val scalatest          = "3.0.0"
  val scalatestplus_play = "1.5.1"

  val findbugs = "3.0.1"
}

object Library {
  val scalaCompiler: ModuleID = "org.scala-lang" % "scala-compiler" % Version.scala
  val scalaReflect : ModuleID = "org.scala-lang" % "scala-reflect" % Version.scala
  val scalaXml     : ModuleID = "org.scala-lang.modules" %% "scala-xml" % Version.scalaXml

  val macroParadise: ModuleID = "org.scalamacros" % "paradise" % Version.macroParadise cross CrossVersion.full
  val kindProjector: ModuleID = "org.spire-math" %% "kind-projector" % Version.kindProjector

  val cats     : ModuleID = "org.typelevel" %% "cats" % Version.cats
  val shapeless: ModuleID = "com.chuusai" %% "shapeless" % Version.shapeless

  val jodaTime   : ModuleID = "joda-time" % "joda-time" % Version.jodaTime
  val jodaConvert: ModuleID = "org.joda" % "joda-convert" % Version.jodaConvert

  val play       : ModuleID = "com.typesafe.play" %% "play" % Version.play
  val playCache  : ModuleID = "com.typesafe.play" %% "play-cache" % Version.play
  val playJdbcApi: ModuleID = "com.typesafe.play" %% "play-jdbc-api" % Version.play
  val playJdbc   : ModuleID = "com.typesafe.play" %% "play-jdbc" % Version.play
  val playJson   : ModuleID = "com.typesafe.play" %% "play-json" % Version.play
  val playTest   : ModuleID = "com.typesafe.play" %% "play-test" % Version.play
  val playWs     : ModuleID = "com.typesafe.play" %% "play-ws" % Version.play

  val anorm: ModuleID = "com.typesafe.play" %% "anorm" % Version.anorm

  val slick        : ModuleID = "com.typesafe.slick" %% "slick" % Version.slick
  val slickHikariCP: ModuleID = "com.typesafe.slick" %% "slick-hikaricp" % Version.slick
  val slickPg      : ModuleID = "com.github.tminglei" %% "slick-pg" % Version.slickPg

  val h2        : ModuleID = "com.h2database" % "h2" % Version.h2
  val postgresql: ModuleID = "org.postgresql" % "postgresql" % Version.postgresql

  val guava      : ModuleID = "com.google.guava" % "guava" % Version.guava
  val jsoup      : ModuleID = "org.jsoup" % "jsoup" % Version.jsoup
  val ficus      : ModuleID = "com.iheart" %% "ficus" % Version.ficus
  val scalaGuice : ModuleID = "net.codingwell" %% "scala-guice" % Version.scalaGuice
  val htmlcleaner: ModuleID = "net.sourceforge.htmlcleaner" % "htmlcleaner" % Version.htmlcleaner

  val commonsIO   : ModuleID = "commons-io" % "commons-io" % Version.commonsIO
  val commonsCodec: ModuleID = "commons-codec" % "commons-codec" % Version.commonsCodec
  val commonsLang3: ModuleID = "org.apache.commons" % "commons-lang3" % Version.commonsLang3

  val logbackClassic: ModuleID = "ch.qos.logback" % "logback-classic" % Version.logbackClassic

  val scalatest         : ModuleID = "org.scalatest" %% "scalatest" % Version.scalatest
  val scalatestplus_play: ModuleID = "org.scalatestplus.play" %% "scalatestplus-play" % Version.scalatestplus_play

  val findbugs: ModuleID = "com.google.code.findbugs" % "jsr305" % Version.findbugs
}
