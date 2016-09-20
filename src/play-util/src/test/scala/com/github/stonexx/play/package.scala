package com.github.stonexx

package object play {
  import com.github.stonexx.scala.data.{SortableEnumeration, OrderedEnumeration}

  object TestEnum extends Enumeration {
    type TestEnum = Value
    val A = Value
    val B = Value
    val C = Value
  }

  object SortEnum extends SortableEnumeration {
    type SortEnum = Value
    val A = Value
    val B = Value
    val C = Value
  }

  object OrderedEnum extends OrderedEnumeration {
    type OrderedEnum = Value
    val A = Value
    val B = Value
    val C = Value
  }
}
