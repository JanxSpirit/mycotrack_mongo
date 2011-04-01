package com.mycotrack.snippet

import xml.NodeSeq
import com.mongodb.casbah.commons.MongoDBObject
import _root_.net.liftweb.util.Helpers
import com.mycotrack.db.MycoMongoDb
import xml.{Group, NodeSeq, Text}
import Helpers._

/**
 * @author chris_carrier
 * @version Nov 28, 2010
 */


class ManageProject {

  def qrCode(xhtml: NodeSeq): NodeSeq = {
    val project = SelectedProject.is.open_!

    Helpers.bind("project", xhtml,
      "image" -> {
        val photo = MycoMongoDb.gridFs.findOne(MongoDBObject("mapped_id" -> project.id))
        photo match {
          case None => Text("No QR code found")
          case Some(photo) => {
            val url = "/images/%s" format photo.id
            <img src={url}/>
          }
        }
      }
    )
  }

}