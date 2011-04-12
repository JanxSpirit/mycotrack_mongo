package com.mycotrack.model

import net.liftweb.record.field._
import net.liftweb.mongodb.record._
import net.liftweb.mongodb.record.field._

class Culture extends MongoRecord[Culture] with MongoId[Culture] {

  def meta = Culture

  object name extends StringField(this, 100) {
    override def displayName = "Name"
  }

  object key extends StringField(this, 100) {
    override def displayName = "Key"
  }

  object createdDate extends DateTimeField(this) {
    override def displayName = "Date Started"
  }

  object substrate extends StringField(this, 100) {
    override def displayName = "Substrate"
  }

  object cultureType extends StringField(this, 100) {
    override def displayName = "Culture Type"
  }

  object userId extends ObjectIdField(this) {
    def obj = User.find(value)
  }

  object species extends ObjectIdField(this){
    def obj = Species.find(value)
  }

}

object Culture extends Culture with MongoMetaRecord[Culture] {
  override def fieldOrder = List(createdDate)
}