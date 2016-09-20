package com.github.stonexx.scala

package object util {

  object all extends ToAllOps
  object cls extends ToClassOps
  object date extends ToDateOps
  object file extends ToFileOps
  object hangul {
    object all extends ToHangulCharOps with ToHangulStringOps
    object char extends ToHangulCharOps
    object string extends ToHangulStringOps
  }
  object path extends ToPathOps
  object product extends ToProductOps
  object string extends ToStringOps
  object throwable extends ToThrowableOps
  object traversable extends ToTraversableOps

  trait ToAllOps
    extends ToClassOps
      with ToDateOps
      with ToFileOps
      with ToHangulCharOps
      with ToHangulStringOps
      with ToPathOps
      with ToProductOps
      with ToStringOps
      with ToThrowableOps
      with ToTraversableOps
}
