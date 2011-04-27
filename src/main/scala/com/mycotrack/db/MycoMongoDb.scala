package com.mycotrack.db

import _root_.net.liftweb.mongodb._
import _root_.net.liftweb.util._
import _root_.net.liftweb.common._
import net.liftweb.json.DefaultFormats
import net.liftweb.json.JsonAST._
import net.liftweb.json.JsonParser._
import net.liftweb.json.JsonDSL._
import com.mongodb.casbah.gridfs.Imports._
import com.mongodb.{Mongo, BasicDBObject, BasicDBObjectBuilder, DBObject}
import com.mongodb.casbah.MongoConnection
import com.mongodb._
import com.mongodb.casbah.Imports._
import net.liftweb.json.JsonDSL._

object MycoMongoDb {

  def gridFs = {
    GridFS(MongoConnection(Props.get("mongodb_host").get,  Props.get("mongodb_port").get.toInt)(Props.get("images_gridfs_name").get));
  }

  def setup {
    Props.requireOrDie("mongodb_host", "mongodb_port", "mongodb_db_name", "images_gridfs_name")
    MongoDB.defineDb(DefaultMongoIdentifier, MongoAddress(MongoHost(Props.get("mongodb_host").get, Props.get("mongodb_port").get.toInt), Props.get("mongodb_db_name").get))
  }

  def isMongoRunning: Boolean = {
    try {
      MongoDB.use(DefaultMongoIdentifier)(db => {
        db.getLastError
      })
      true
    }
    catch {
      case e => false
    }
  }

  def incrementMasterCounter: Int = {

    val seqColl = MongoConnection()("mycotrack")("seq")

    val seq = seqColl.findAndModify(MongoDBObject("_id" -> "projects"), MongoDBObject("seq" -> 1), MongoDBObject("_id" -> 1), false, com.mongodb.casbah.Imports.$inc ("seq" -> 1), true, true).get
    seq.getAs[Int]("seq").get

    //val counter = counterColl.findOne.get

    //counterColl.update(counter, $inc("value" -> 1), true, false)
    //counterColl.update(counter, "value" -> 1, true, false)

  }

}