package com.github.stonexx.slick.ext

import slick.basic.BasicProfile

trait HasSlickProfile[+P <: BasicProfile] {
  val profile: P
}
