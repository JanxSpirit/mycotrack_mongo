package com.mycotrack.snippet

import xml.NodeSeq
import com.mongodb.casbah.commons.MongoDBObject
import _root_.net.liftweb.util.Helpers
import com.mycotrack.db.MycoMongoDb
import xml.{Group, NodeSeq, Text}
import Helpers._
import net.liftweb.http.{RequestVar, S, TemplateFinder, SHtml}
import com.mycotrack.model.{Species, Project, User}
import net.liftweb.common.{Empty, Full}
import com.mycotrack.lib.ChartGenerator


/**
 * @author chris_carrier
 * @version Nov 28, 2010
 */


class ManageProject extends ChartGenerator {

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

  def splitProject(xhtml: NodeSeq): NodeSeq = {
    val project = SelectedProject.is.open_!

    val newProject = Project.createRecord
    val createdDate = newProject.createdDate

    Helpers.bind("entry", xhtml,
      "substratePreparation" -> SHtml.select(List("none" -> "none", "pasteurized" -> "pasteurized", "sterilized" -> "sterilized"), Full("none"), newProject.preparation(_)),
      "container" -> SHtml.select(List("none" -> "none", "Jar - quart" -> "Jar - quart", "Jar - pint" -> "Jar - pint", "Bag - filter" -> "Bag - filter"), Full("none"), newProject.container(_)),
      "substrate" -> SHtml.text("", newProject.substrate(_)),
      "createdDate" -> createdDate.toForm,
      "submit" -> SHtml.submit("Create", () => {
        newProject.userId.set(project.userId.is)
        newProject.species(project.species.is)
        newProject.parentProject(project.id)
        //proj.createdDate(new Date)
        newProject.save;
        createQRCode("www.google.com", Empty, Empty, newProject.id)
        S.redirectTo("/manage")
      }))
  }

}