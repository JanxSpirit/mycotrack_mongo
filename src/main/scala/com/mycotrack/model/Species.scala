package com.mycotrack.model

import net.liftweb.record.field._
import net.liftweb.mongodb.record._

class Species extends MongoRecord[Species] with MongoId[Species] {

  def meta = Species

  object commonName extends StringField(this,100){
    override def displayName = "Common Name"
  }

  object scientificName extends StringField(this,100){
    override def displayName = "Scientific Name"
  }

  object infoUrl extends StringField(this,100){
    override def displayName = "Link"
  }
}

object Species extends Species with MongoMetaRecord[Species] {
  //override def fieldOrder = List(commonName, scientificName)

  override def collectionName="species"
}