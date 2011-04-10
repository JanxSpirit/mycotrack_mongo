package com.mycotrack.snippet

import _root_.scala.xml.NodeSeq
import _root_.net.liftweb.util.Helpers
import net.liftweb.http.{S, SHtml}
import Helpers._
import com.mycotrack.model.{Species, Project}
import net.liftweb.common.{Logger, Full, Empty}
import net.liftweb.mongodb.{JsonObject, JsonObjectMeta}
import com.mongodb._
import com.mongodb.casbah.Imports._

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
    
    val speciesList = Species.findAll

    Helpers.bind("entry", xhtml,
      "species" -> SHtml.select(speciesList.map(s => s.commonName.is -> s.commonName.is), Empty, proj.species(_)),
      "name" -> SHtml.text(name, name = _),
      //"createdDate" -> createdDate.toForm,
      "submit" -> SHtml.submit("Submit", () => {
        proj.name(name);
        //proj.species(species);
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