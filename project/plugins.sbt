logLevel := Level.Warn

resolvers += Resolver.typesafeRepo("releases")

// The Play plugin
addSbtPlugin("com.typesafe.play" % "sbt-plugin" % sys.props.getOrElse("play.version", "2.6.6"))

// Scala formatting: "sbt scalafmt"
// https://olafurpg.github.io/scalafmt
addSbtPlugin("com.geirsson" % "sbt-scalafmt" % "0.3.1")

// web plugins
addSbtPlugin("com.typesafe.sbt" % "sbt-coffeescript" % "1.0.1")
addSbtPlugin("com.typesafe.sbt" % "sbt-less" % "1.1.2")
addSbtPlugin("com.typesafe.sbt" % "sbt-jshint" % "1.0.5")
addSbtPlugin("com.typesafe.sbt" % "sbt-rjs" % "1.0.9")
addSbtPlugin("com.typesafe.sbt" % "sbt-digest" % "1.1.3")
addSbtPlugin("com.typesafe.sbt" % "sbt-gzip" % "1.0.2")
addSbtPlugin("com.typesafe.sbt" % "sbt-mocha" % "1.1.2")
addSbtPlugin("com.typesafe.sbt" % "sbt-uglify" % "2.0.0")
addSbtPlugin("org.irundaia.sbt" % "sbt-sassify" % "1.4.10")
addSbtPlugin("com.github.ddispaltro" % "sbt-reactjs" % "0.6.8")
addSbtPlugin("com.slidingautonomy.sbt" % "sbt-html-minifier" % "1.0.0")

// etc plugins
addSbtPlugin("io.get-coursier" % "sbt-coursier" % "1.0.0-RC12")
addSbtPlugin("org.foundweekends" % "sbt-bintray" % "0.5.1" exclude ("org.slf4j", "slf4j-nop"))
addSbtPlugin("net.virtual-void" % "sbt-dependency-graph" % "0.8.2")
addSbtPlugin("org.ensime" % "sbt-ensime" % "2.0.1")
