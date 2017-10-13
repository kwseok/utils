# Utilities for Scala, Slick & Play [![Build Status](https://travis-ci.org/stonexx/utils.svg?branch=master)](https://travis-ci.org/stonexx/utils)

- [play-util](https://github.com/stonexx/utils/tree/master/src/play-util)
- [slick-ext](https://github.com/stonexx/utils/tree/master/src/slick-ext)
- [slick-pg](https://github.com/stonexx/utils/tree/master/src/slick-pg)
- [scala-util](https://github.com/stonexx/utils/tree/master/src/scala-util)

Installation
------------

Add scala version to your project build.sbt (supports scala 2.11 or 2.12):
```scala
scalaVersion := "2.11.11"
```

Add resolver to project build.sbt:
```scala
resolvers += Resolver.jcenterRepo
```

> If you need `play-util` support, add dependency:
```scala
libraryDependencies += "com.github.stonexx" %% "play-util" % "0.6.6"
```

> If you need `slick-ext` support, add dependency:
```scala
libraryDependencies += "com.github.stonexx" %% "slick-ext" % "0.6.6"
```

> If you need `slick-pg` support, add dependency:
```scala
libraryDependencies += "com.github.stonexx" %% "slick-pg" % "0.6.6"
```

> If you need `scala-util` support, add dependency:
```scala
libraryDependencies += "com.github.stonexx" %% "scala-util" % "0.6.6"
```
