package com.github.stonexx.play.db.anorm

import anorm._
import org.postgresql.util.PGobject

import scala.collection.JavaConverters._
import scala.util.{Success, Failure, Try}

trait PgExtensions {
  private[anorm] val mathContext = new java.math.MathContext(4)

  implicit def columnToMap: Column[Map[String, String]] = Column.nonNull { (value, _) =>
    value match {
      case sm: java.util.HashMap[_, _] => Try {
        sm.asInstanceOf[java.util.HashMap[String, String]].asScala.toMap
      } match {
        case Success(m) => Right(m)
        case Failure(e) => Left(TypeDoesNotMatch(e.toString))
      }
      case sa: java.sql.Array => Try {
        sa.getArray.asInstanceOf[Array[Array[String]]].map(e => e(0) -> e(1)).toMap
      } match {
        case Success(a) => Right(a)
        case Failure(e) => Left(TypeDoesNotMatch(e.toString))
      }
      case x => Left(TypeDoesNotMatch(x.getClass.toString))
    }
  }

  implicit def mapToStatement: ToStatement[Map[String, String]] = new ToStatement[Map[String, String]] {
    def set(s: java.sql.PreparedStatement, index: Int, aValue: Map[String, String]) {
      s.setObject(index, new java.util.HashMap[String, String](aValue.asJava))
    }
  }

  case class LTree(value: Seq[String])

  object LTree {
    implicit def rowToStringSeq: Column[LTree] = Column.nonNull { (value, _) =>
      value match {
        case pgo: PGobject => pgo.getType match {
          case "ltree" => Right(LTree(pgo.getValue.split('.')))
          case x => Left(TypeDoesNotMatch(x.getClass.toString))
        }
        case x => Left(TypeDoesNotMatch(x.getClass.toString))
      }
    }

    implicit def stringSeqToStatement: ToStatement[LTree] = new ToStatement[LTree] {
      def set(s: java.sql.PreparedStatement, index: Int, aValue: LTree) {
        val pgo = new PGobject
        pgo.setType("ltree")
        pgo.setValue(aValue.value.mkString("."))
        s.setObject(index, pgo)
      }
    }
  }
}
