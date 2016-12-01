# Libraries for Scala, Slick & Play [![Build Status](https://travis-ci.org/stonexx/lib.svg?branch=master)](https://travis-ci.org/stonexx/lib)

- [play-anorm](https://github.com/stonexx/lib/tree/master/src/play-anorm)
- [play-slick](https://github.com/stonexx/lib/tree/master/src/play-slick)
- [play-slick-jdbc-adapter](https://github.com/stonexx/lib/tree/master/src/play-slick-jdbc-adapter)
- [play-util](https://github.com/stonexx/lib/tree/master/src/play-util)
- [slick-ext](https://github.com/stonexx/lib/tree/master/src/slick-ext)
- [slick-pg](https://github.com/stonexx/lib/tree/master/src/slick-pg)
- [scala-util](https://github.com/stonexx/lib/tree/master/src/scala-util)

Installation
------------

Add scala version to your project build.sbt (only supports scala 2.11):
```scala
scalaVersion := "2.11.8"
```

Add resolver to project build.sbt:
```scala
resolvers += Resolver.bintrayRepo("stonexx", "maven")
```

> If you need `play-anorm` support, add dependency:
```scala
libraryDependencies += "com.github.stonexx" %% "play-anorm" % "0.6.5-M1"
```

> If you need `play-slick` support, add dependency:
```scala
libraryDependencies += "com.github.stonexx" %% "play-slick" % "0.6.5-M1"
```

> If you need `play-slick-jdbc-adapter` support, add dependency:
```scala
libraryDependencies += "com.github.stonexx" %% "play-slick-jdbc-adapter" % "0.6.5-M1"
```

> If you need `play-util` support, add dependency:
```scala
libraryDependencies += "com.github.stonexx" %% "play-util" % "0.6.5-M1"
```

> If you need `slick-ext` support, add dependency:
```scala
libraryDependencies += "com.github.stonexx" %% "slick-ext" % "0.6.5-M1"
```

> If you need `slick-pg` support, add dependency:
```scala
libraryDependencies += "com.github.stonexx" %% "slick-pg" % "0.6.5-M1"
```

> If you need `scala-util` support, add dependency:
```scala
libraryDependencies += "com.github.stonexx" %% "scala-util" % "0.6.5-M1"
```
