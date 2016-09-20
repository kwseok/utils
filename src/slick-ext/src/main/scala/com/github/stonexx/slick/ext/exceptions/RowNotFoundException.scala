package com.github.stonexx.slick.ext.exceptions

import slick.SlickException

class RowNotFoundException[T](val notFoundId: T) extends SlickException(s"Row not found: $notFoundId")
