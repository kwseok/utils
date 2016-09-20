package com.github.stonexx.slick.pg.syntax

import slick.ast.{LiteralNode, TypedType}
import slick.ast.Library.{SqlOperator, JdbcFunction, SqlFunction}
import slick.ast.ScalaBaseType._
import slick.lifted._
import slick.lifted.FunctionSymbolExtensionMethods._

object SyntaxLibrary {
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

  /// pg_trgm functions & operators
  val Similarity = new SqlFunction("similarity")
  val ShowTrgm   = new SqlFunction("show_trgm")
  val SetLimit   = new SqlFunction("set_limit")
  val ShowLimit  = new SqlFunction("show_limit")
  val %-%        = new SqlOperator("%")
  val <->        = new SqlOperator("<->")

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

trait SyntaxAssistants {
  def similarity[P1, P2, R](a: Rep[P1], b: Rep[P2])(
    implicit
    tm: TypedType[P1],
    om: OptionMapperDSL.arg[String, P1]#arg[String, P2]#to[Float, R]
  ) = om.column(SyntaxLibrary.Similarity, a.toNode, b.toNode)

  def showTrgm[P1, R](a: Rep[P1])(
    implicit
    tm1: TypedType[P1], tm2: TypedType[Seq[String]],
    om: OptionMapperDSL.arg[String, P1]#to[Seq[String], R]
  ) = om.column(SyntaxLibrary.ShowTrgm, a.toNode)

  def setLimit[P1, R](a: Rep[P1])(
    implicit
    tm: TypedType[P1],
    om: OptionMapperDSL.arg[Float, P1]#to[Float, R]
  ) = om.column(SyntaxLibrary.SetLimit, a.toNode)

  def showLimit: Rep[Float] = SyntaxLibrary.ShowLimit.column[Float]()
}

final class StringColumnExtensionMethods[P1](val c: Rep[P1]) extends AnyVal with ExtensionMethods[String, P1] {
  protected[this] implicit def b1Type = implicitly[TypedType[String]]

  def ~<~[P2, R](e: Rep[P2])(implicit om: o#arg[String, P2]#to[Boolean, R]) = om.column(SyntaxLibrary.~<~, n, e.toNode)
  def ~<=~[P2, R](e: Rep[P2])(implicit om: o#arg[String, P2]#to[Boolean, R]) = om.column(SyntaxLibrary.~<=~, n, e.toNode)
  def ~>~[P2, R](e: Rep[P2])(implicit om: o#arg[String, P2]#to[Boolean, R]) = om.column(SyntaxLibrary.~>~, n, e.toNode)
  def ~>=~[P2, R](e: Rep[P2])(implicit om: o#arg[String, P2]#to[Boolean, R]) = om.column(SyntaxLibrary.~>=~, n, e.toNode)

  def ~[P2, R](e: Rep[P2])(implicit om: o#arg[String, P2]#to[Boolean, R]) = om.column(SyntaxLibrary.~, n, e.toNode)
  def ~*[P2, R](e: Rep[P2])(implicit om: o#arg[String, P2]#to[Boolean, R]) = om.column(SyntaxLibrary.~*, n, e.toNode)
  def !~[P2, R](e: Rep[P2])(implicit om: o#arg[String, P2]#to[Boolean, R]) = om.column(SyntaxLibrary.!~, n, e.toNode)
  def !~*[P2, R](e: Rep[P2])(implicit om: o#arg[String, P2]#to[Boolean, R]) = om.column(SyntaxLibrary.!~*, n, e.toNode)

  def ~~[P2, R](e: Rep[P2], esc: Char = '\u0000')(implicit om: o#arg[String, P2]#to[Boolean, R]) =
    if (esc == '\u0000') om.column(SyntaxLibrary.~~, n, e.toNode)
    else om.column(SyntaxLibrary.~~, n, e.toNode, LiteralNode(esc))
  def ~~*[P2, R](e: Rep[P2], esc: Char = '\u0000')(implicit om: o#arg[String, P2]#to[Boolean, R]) =
    if (esc == '\u0000') om.column(SyntaxLibrary.~~*, n, e.toNode)
    else om.column(SyntaxLibrary.~~*, n, e.toNode, LiteralNode(esc))
  def ilike[P2, R](e: Rep[P2], esc: Char = '\u0000')(implicit om: o#arg[String, P2]#to[Boolean, R]) =
    if (esc == '\u0000') om.column(SyntaxLibrary.ILike, n, e.toNode)
    else om.column(SyntaxLibrary.ILike, n, e.toNode, LiteralNode(esc))

  def similarity[P2, R](e: Rep[P2])(implicit om: o#arg[String, P2]#to[Float, R]) = om.column(SyntaxLibrary.Similarity, n, e.toNode)
  def %-%[P2, R](e: Rep[P2])(implicit om: o#arg[String, P2]#to[Boolean, R]) = om.column(SyntaxLibrary.%-%, n, e.toNode)
  def <->[P2, R](e: Rep[P2])(implicit om: o#arg[String, P2]#to[Float, R]) = om.column(SyntaxLibrary.<->, n, e.toNode)

  def regexpReplace[P2, P3, R](pattern: Rep[P2], replacement: Rep[P3], flags: Option[String] = None)(
    implicit
    om: o#arg[String, P2]#arg[String, P3]#to[String, R]
  ) = flags match {
    case None => om.column(SyntaxLibrary.RegexpReplace, n, pattern.toNode, replacement.toNode)
    case Some(f) => om.column(SyntaxLibrary.RegexpReplace, n, pattern.toNode, replacement.toNode, LiteralNode(f))
  }

  def regexpMatches[P2](pattern: Rep[P2], flags: Option[String] = None)(
    implicit
    tm: TypedType[Seq[String]],
    om: o#arg[String, P2]#to[String, _],
    unpack: Shape[_ <: FlatShapeLevel, Rep[Seq[String]], Seq[String], Rep[Seq[String]]]
  ) = Query(flags match {
    case None => SyntaxLibrary.RegexpMatches.column[Seq[String]](n, pattern.toNode)
    case Some(f) => SyntaxLibrary.RegexpMatches.column[Seq[String]](n, pattern.toNode, LiteralNode(f))
  })

  def regexpSplitToArray[P2, R](pattern: Rep[P2], flags: Option[String] = None)(
    implicit
    tm: TypedType[Seq[String]],
    om: o#arg[String, P2]#to[Seq[String], R]
  ) = flags match {
    case None => om.column(SyntaxLibrary.RegexpSplitToArray, n, pattern.toNode)
    case Some(f) => om.column(SyntaxLibrary.RegexpSplitToArray, n, pattern.toNode, LiteralNode(f))
  }

  def regexpSplitToTable[P2](pattern: Rep[P2], flags: Option[String] = None)(
    implicit
    om: o#arg[String, P2]#to[String, _]
  ) = Query(flags match {
    case None => SyntaxLibrary.RegexpSplitToTable.column[String](n, pattern.toNode)
    case Some(f) => SyntaxLibrary.RegexpSplitToTable.column[String](n, pattern.toNode, LiteralNode(f))
  })
}

trait ExtensionMethodConversions {
  implicit def pgSyntaxStringColumnExtensionMethods(c: Rep[String]): StringColumnExtensionMethods[String] = new StringColumnExtensionMethods[String](c)
  implicit def pgSyntaxStringOptionColumnExtensionMethods(c: Rep[Option[String]]): StringColumnExtensionMethods[Option[String]] = new StringColumnExtensionMethods[Option[String]](c)
}
