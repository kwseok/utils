package com.github.stonexx.slick.ext.exceptions

import slick.SlickException

class TooManyRowsAffectedException(val affectedRowCount: Int, val expectedRowCount: Int)
  extends SlickException(s"Expected $expectedRowCount row(s) affected, got $affectedRowCount instead")
