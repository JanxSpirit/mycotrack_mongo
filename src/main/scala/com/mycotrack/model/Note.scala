package com.mycotrack.model

import net.liftweb.record.field._
import net.liftweb.mongodb.record._
import net.liftweb.mongodb.record.field._
import java.util.Date
import net.liftweb.mongodb.{JsonObject, JsonObjectMeta}

case class Note(name: String, body: String) extends JsonObject[Note] {

  def meta = Note

}

object Note extends JsonObjectMeta[Note] {
  //override def fieldOrder = List(createdDate, name, species)
}