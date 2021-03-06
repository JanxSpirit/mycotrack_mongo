package com.mycotrack.model

import _root_.net.liftweb.record.field._
import _root_.net.liftweb.common._

import com.mycotrack.db._
import com.mycotrack.lib._
import net.liftweb.mongodb.record.field.{MongoListField, MongoJsonObjectListField}
import com.mongodb.{BasicDBList, DBObject, BasicDBObject}
import org.bson.types.ObjectId

/**
 * An O-R mapped "User" class that includes first name, last name, password and we add a "Personal Essay" to it
 */
class User extends MongoMegaProtoUser[User] {
  def meta = User // what's the "meta" server

  // define an additional field for a personal essay
  object textArea extends StringField(this, 2048) {
    override def displayName = "Personal Essay"
  }

  // these require custom setFromDBObject methods
  object cultureList extends MongoListField[User, ObjectId](this) {
    def obj: List[Culture] = {
      value match {
        case x: ObjectId => x.map(xs => Culture.find(xs).open_!)
        case _ => List.empty
      }
    }
  }
}

/**
 * The singleton that has methods for accessing the database
 */
object User extends User with MongoMetaMegaProtoUser[User] {
  override def screenWrap = Full(<lift:surround with="default" at="content">
			       <lift:bind /></lift:surround>)

  // comment this line out to require email validations
  override def skipEmailValidation = true
}
