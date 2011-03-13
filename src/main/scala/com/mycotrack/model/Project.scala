package com.mycotrack.model

import net.liftweb.record.field._
import net.liftweb.mongodb.record._
import net.liftweb.mongodb.record.field._
import java.util.Date

class Project extends MongoRecord[Project] with MongoId[Project] {

  def meta = Project

  object createdDate extends DateTimeField(this){
    override def displayName = "Date Started"
  }

  object name extends StringField(this,100){
    override def displayName = "Name"
  }

  object species extends StringField(this,100){
    override def displayName = "Species"
  }

  object userId extends ObjectIdField(this) {
    def obj = User.find(value)
  }

  /*object events extends MongoCaseClassListField[Project, Event](this) {

  } */
}

object Project extends Project with MongoMetaRecord[Project] {
  //override def fieldOrder = List(createdDate, name, species)
}

case class Event(name: String, eventType: String, eventDate: Date)