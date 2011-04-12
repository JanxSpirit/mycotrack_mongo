package com.mycotrack.snippet

import _root_.scala.xml.NodeSeq
import _root_.net.liftweb.util.Helpers
import net.liftweb.http.{S, SHtml}
import Helpers._
import com.mycotrack.model.{Species, Project, Culture, User}
import net.liftweb.common.{Logger, Full, Empty}
import net.liftweb.mongodb.{JsonObject, JsonObjectMeta}
import com.mongodb._
import com.mongodb.casbah.Imports._
import com.mongodb.casbah.Implicits._
import net.liftweb.json.JsonDSL._


/**
 * @author chris_carrier
 * @version Nov 9, 2010
 */


class EditProject extends Logger {

  def edit(xhtml: NodeSeq): NodeSeq = {
    val proj = SelectedProject.is.open_!
    var name = proj.name.is
    var notes = proj.notes
    //var species: Species = proj.species.obj.open_!
    var createdDate = proj.createdDate.is
    var culture = proj.culture
    val tempUser = User.currentUser.open_!
    
    val speciesList = Species.findAll

    Helpers.bind("entry", xhtml,
      "culture" -> SHtml.select(Culture.findAll("userId" -> tempUser.id.toString).map(xs => xs.key.is -> xs.id.toString), Empty, culture.setFromString(_)),
      "name" -> SHtml.text(name, name = _),
      //"createdDate" -> createdDate.toForm,
      "submit" -> SHtml.submit("Submit", () => {
        proj.name(name);
        proj.save;
        S.redirectTo("/manage", () => SelectedProject(Full(proj)));
      }))

    //proj.toForm(Full("save"), {_.save})
  }

  def splitLink(xhtml: NodeSeq): NodeSeq = {
    val project = SelectedProject.is.open_!
    val url = "/splitProject/" + project.id.toString
    <span><a href={url}>Split project</a></span>
  }
}