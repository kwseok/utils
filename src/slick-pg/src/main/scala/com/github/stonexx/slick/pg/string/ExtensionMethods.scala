package com.github.stonexx.slick.pg.string

import slick.ast.{LiteralNode, TypedType}
import slick.ast.Library.{SqlOperator, JdbcFunction}
import slick.ast.ScalaBaseType._
import slick.lifted._
import slick.lifted.FunctionSymbolExtensionMethods._

object StringLibrary {
  /// string comparison operators
  val ~<~  = new SqlOperator("~<~")
  val ~<=~ = new SqlOperator("~<=~")
  val ~>~  = new SqlOperator("~>~")
  val ~>=~ = new SqlOperator("~>=~")

  // Matches regular expression, case sensitive	'thomas' ~ '.*thomas.*'
  val ~   = new SqlOperator("~")
  // Matches regular expression, case insensitive	'thomas' ~* '.*Thomas.*'
  val ~*  = new SqlOperator("~*")
  // Does not match regular expression, case sensitive	'thomas' !~ '.*Thomas.*'
  val !~  = new SqlOperator("!~")
  // Does not match regular expression, case insensitive	'thomas' !~* '.*vadim.*'
  val !~* = new SqlOperator("!~*")

  // like
  val ~~    = new SqlOperator("~~")
  // ilike
  val ~~*   = new SqlOperator("~~*")
  val ILike = new SqlOperator("ilike")

  /// Other string functions
  //Replace substring(s) matching a POSIX regular expression.
  val RegexpReplace      = new JdbcFunction("regexp_replace")
  //Return all captured substrings resulting from matching a POSIX regular expression against the string.
  val RegexpMatches      = new JdbcFunction("regexp_matches")
  //Split string using a POSIX regular expression as the delimiter.
  val RegexpSplitToArray = new JdbcFunction("regexp_split_to_array")
  //Split string using a POSIX regular expression as the delimiter.
  val RegexpSplitToTable = new JdbcFunction("regexp_split_to_table")
}

final class StringColumnExtensionMethods[P1](val c: Rep[P1]) extends AnyVal with ExtensionMethods[String, P1] {
  protected[this] implicit def b1Type: TypedType[String] = implicitly[TypedType[String]]

  def ~<~[P2, R](e: Rep[P2])(implicit om: o#arg[String, P2]#to[Boolean, R]): Rep[R] = om.column(StringLibrary.~<~, n, e.toNode)
  def ~<=~[P2, R](e: Rep[P2])(implicit om: o#arg[String, P2]#to[Boolean, R]): Rep[R] = om.column(StringLibrary.~<=~, n, e.toNode)
  def ~>~[P2, R](e: Rep[P2])(implicit om: o#arg[String, P2]#to[Boolean, R]): Rep[R] = om.column(StringLibrary.~>~, n, e.toNode)
  def ~>=~[P2, R](e: Rep[P2])(implicit om: o#arg[String, P2]#to[Boolean, R]): Rep[R] = om.column(StringLibrary.~>=~, n, e.toNode)

  def ~[P2, R](e: Rep[P2])(implicit om: o#arg[String, P2]#to[Boolean, R]): Rep[R] = om.column(StringLibrary.~, n, e.toNode)
  def ~*[P2, R](e: Rep[P2])(implicit om: o#arg[String, P2]#to[Boolean, R]): Rep[R] = om.column(StringLibrary.~*, n, e.toNode)
  def !~[P2, R](e: Rep[P2])(implicit om: o#arg[String, P2]#to[Boolean, R]): Rep[R] = om.column(StringLibrary.!~, n, e.toNode)
  def !~*[P2, R](e: Rep[P2])(implicit om: o#arg[String, P2]#to[Boolean, R]): Rep[R] = om.column(StringLibrary.!~*, n, e.toNode)

  def ~~[P2, R](e: Rep[P2], esc: Char = '\u0000')(implicit om: o#arg[String, P2]#to[Boolean, R]): Rep[R] =
    if (esc == '\u0000') om.column(StringLibrary.~~, n, e.toNode)
    else om.column(StringLibrary.~~, n, e.toNode, LiteralNode(esc))
  def ~~*[P2, R](e: Rep[P2], esc: Char = '\u0000')(implicit om: o#arg[String, P2]#to[Boolean, R]): Rep[R] =
    if (esc == '\u0000') om.column(StringLibrary.~~*, n, e.toNode)
    else om.column(StringLibrary.~~*, n, e.toNode, LiteralNode(esc))
  def ilike[P2, R](e: Rep[P2], esc: Char = '\u0000')(implicit om: o#arg[String, P2]#to[Boolean, R]): Rep[R] =
    if (esc == '\u0000') om.column(StringLibrary.ILike, n, e.toNode)
    else om.column(StringLibrary.ILike, n, e.toNode, LiteralNode(esc))

  def regexpReplace[P2, P3, R](pattern: Rep[P2], replacement: Rep[P3], flags: Option[String] = None)(
    implicit
    om: o#arg[String, P2]#arg[String, P3]#to[String, R]
  ): Rep[R] = flags match {
    case None => om.column(StringLibrary.RegexpReplace, n, pattern.toNode, replacement.toNode)
    case Some(f) => om.column(StringLibrary.RegexpReplace, n, pattern.toNode, replacement.toNode, LiteralNode(f))
  }

  def regexpMatches[P2](pattern: Rep[P2], flags: Option[String] = None)(
    implicit
    tm: TypedType[Seq[String]],
    om: o#arg[String, P2]#to[String, _],
    unpack: Shape[_ <: FlatShapeLevel, Rep[Seq[String]], Seq[String], Rep[Seq[String]]]
  ) = Query(flags match {
    case None => StringLibrary.RegexpMatches.column[Seq[String]](n, pattern.toNode)
    case Some(f) => StringLibrary.RegexpMatches.column[Seq[String]](n, pattern.toNode, LiteralNode(f))
  })

  def regexpSplitToArray[P2, R](pattern: Rep[P2], flags: Option[String] = None)(
    implicit
    tm: TypedType[Seq[String]],
    om: o#arg[String, P2]#to[Seq[String], R]
  ): Rep[R] = flags match {
    case None => om.column(StringLibrary.RegexpSplitToArray, n, pattern.toNode)
    case Some(f) => om.column(StringLibrary.RegexpSplitToArray, n, pattern.toNode, LiteralNode(f))
  }

  def regexpSplitToTable[P2](pattern: Rep[P2], flags: Option[String] = None)(
    implicit
    om: o#arg[String, P2]#to[String, _]
  ) = Query(flags match {
    case None => StringLibrary.RegexpSplitToTable.column[String](n, pattern.toNode)
    case Some(f) => StringLibrary.RegexpSplitToTable.column[String](n, pattern.toNode, LiteralNode(f))
  })
}

trait ExtensionMethodConversions {
  implicit def pgStringColumnExtensionMethods(c: Rep[String]): StringColumnExtensionMethods[String] = new StringColumnExtensionMethods[String](c)
  implicit def pgStringOptionColumnExtensionMethods(c: Rep[Option[String]]): StringColumnExtensionMethods[Option[String]] = new StringColumnExtensionMethods[Option[String]](c)
}
