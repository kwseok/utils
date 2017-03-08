package com.github.stonexx.play.json

import com.github.stonexx.scala.data.OrderedEnumeration
import play.api.libs.json._

trait EnumFormats
  extends EnumIdFormats
    with EnumNameFormats
    with EnumOrderedFormats {

  // aliases

  @inline def enumReads[E <: Enumeration](enum: E): Reads[E#Value] = enumNameReads(enum)
  @inline def enumWrites[E <: Enumeration]: Writes[E#Value] = enumNameWrites[E]
  @inline def enumFormat[E <: Enumeration](enum: E): Format[E#Value] = enumNameFormat(enum)
}

trait EnumIdFormats {

  def enumIdReads[E <: Enumeration](enum: E): Reads[E#Value] = new Reads[E#Value] {
    def reads(json: JsValue): JsResult[E#Value] = json match {
      case JsNumber(n) => try JsSuccess(enum(n.toInt))
      catch {
        case _: NoSuchElementException =>
          JsError(s"Enumeration expected of type: '${enum.getClass}'," +
            s" but it does not appear to contain the value: '$n'")
      }
      case _ => JsError("Number value expected")
    }
  }

  def enumIdWrites[E <: Enumeration]: Writes[E#Value] = new Writes[E#Value] {
    def writes(v: E#Value): JsValue = JsNumber(v.id)
  }

  def enumIdFormat[E <: Enumeration](enum: E): Format[E#Value] = Format(enumIdReads(enum), enumIdWrites)
}

trait EnumNameFormats {

  def enumNameReads[E <: Enumeration](enum: E): Reads[E#Value] = new Reads[E#Value] {
    def reads(json: JsValue): JsResult[E#Value] = json match {
      case JsString(s) => try JsSuccess(enum.withName(s))
      catch {
        case _: NoSuchElementException =>
          JsError(s"Enumeration expected of type: '${enum.getClass}'," +
            s" but it does not appear to contain the value: '$s'")
      }
      case _ => JsError("String value expected")
    }
  }

  def enumNameWrites[E <: Enumeration]: Writes[E#Value] = new Writes[E#Value] {
    def writes(v: E#Value): JsValue = JsString(v.toString)
  }

  def enumNameFormat[E <: Enumeration](enum: E): Format[E#Value] = Format(enumNameReads(enum), enumNameWrites)
}

trait EnumOrderedFormats {

  def enumOrderedReads[E <: OrderedEnumeration](enum: E): Reads[E#Ordered] = new Reads[E#Ordered] {
    def reads(json: JsValue): JsResult[E#Ordered] = json match {
      case JsString(s) => try JsSuccess(enum.Ordered.parse(s))
      catch {
        case _: NoSuchElementException =>
          JsError(s"OrderedEnumeration expected of type: '${enum.getClass}'," +
            s" but it does not appear to contain the value: '$s'")
      }
      case _ => JsError("String value expected")
    }
  }

  def enumOrderedWrites[E <: OrderedEnumeration]: Writes[E#Ordered] = new Writes[E#Ordered] {
    def writes(v: E#Ordered): JsValue = JsString(v.toString)
  }

  def enumOrderedFormat[E <: OrderedEnumeration](enum: E): Format[E#Ordered] = Format(enumOrderedReads(enum), enumOrderedWrites)
}
