package com.mycotrack.model

import net.liftweb.record.field._
import net.liftweb.mongodb.record._
import net.liftweb.mongodb.record.field._
import java.util.Date

class Note extends MongoRecord[Note] with MongoId[Note] {

  def meta = Note

  object createdDate extends DateTimeField(this){
    override def displayName = "Date Started"
  }

  object name extends StringField(this,100){
    override def displayName = "Name"
  }

  object body extends StringField(this,100){
    override def displayName = "body"
  }

}

object Note extends Note with MongoMetaRecord[Note] {
  //override def fieldOrder = List(createdDate, name, species)
}