logLevel := Level.Warn

resolvers += Resolver.typesafeRepo("releases")

// The Play plugin
addSbtPlugin("com.typesafe.play" % "sbt-plugin" % sys.props.getOrElse("play.version", "2.5.8"))

// web plugins
addSbtPlugin("com.typesafe.sbt" % "sbt-coffeescript" % "1.0.0")
addSbtPlugin("com.typesafe.sbt" % "sbt-less" % "1.1.0")
addSbtPlugin("com.typesafe.sbt" % "sbt-jshint" % "1.0.4")
addSbtPlugin("com.typesafe.sbt" % "sbt-rjs" % "1.0.8")
addSbtPlugin("com.typesafe.sbt" % "sbt-digest" % "1.1.1")
addSbtPlugin("com.typesafe.sbt" % "sbt-gzip" % "1.0.0")
addSbtPlugin("com.typesafe.sbt" % "sbt-mocha" % "1.1.0")
addSbtPlugin("com.typesafe.sbt" % "sbt-uglify" % "1.0.3")
addSbtPlugin("org.irundaia.sbt" % "sbt-sassify" % "1.4.6")
addSbtPlugin("com.slidingautonomy.sbt" % "sbt-html-minifier" % "1.0.0")
addSbtPlugin("com.github.dwickern" % "sbt-bower" % "1.0.3")

// etc plugins
addSbtPlugin("me.lessis" % "bintray-sbt" % "0.3.0" exclude ("org.slf4j", "slf4j-nop"))
addSbtPlugin("io.get-coursier" % "sbt-coursier" % "1.0.0-M14-2")
addSbtPlugin("org.ensime" % "sbt-ensime" % "1.9.0")
addSbtPlugin("net.virtual-void" % "sbt-dependency-graph" % "0.8.2")
