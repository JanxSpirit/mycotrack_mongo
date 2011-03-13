package com.mycotrack.db;

	import _root_.net.liftweb.mongodb._
	import _root_.net.liftweb.util._
	import _root_.net.liftweb.common._
	import net.liftweb.json.DefaultFormats
	import net.liftweb.json.JsonAST._
	import net.liftweb.json.JsonParser._
	import net.liftweb.json.JsonDSL._

	import com.mongodb.{BasicDBObject, BasicDBObjectBuilder, DBObject}

object MycoMongoDb {
  	def setup {
	    MongoDB.defineDb(DefaultMongoIdentifier, MongoAddress(MongoHost("anduin", 27017), "mycotrack"))
	}

	def isMongoRunning: Boolean = {
	    try {
	      MongoDB.use(DefaultMongoIdentifier) ( db => { db.getLastError } )
	      true
	    }
	    catch {
	      case e => false
	    }
	  }
}