package com.mycotrack.snippet

import _root_.scala.xml.NodeSeq
import _root_.net.liftweb.util.Helpers
import net.liftweb.http.{S, SHtml}
import Helpers._
import com.mycotrack.model.{Species, Project}
import net.liftweb.common.Full

/**
 * @author chris_carrier
 * @version Nov 9, 2010
 */


class EditProject {

  def edit(xhtml: NodeSeq): NodeSeq = {
    val proj = SelectedProject.is.open_!
    var name = proj.name.is
    var notes = proj.notes
    //var species = proj.species.is
    //var createdDate = proj.createdDate

    Helpers.bind("entry", xhtml,
      //"species" -> SHtml.select(Species.findAll.map(s => s.commonName.is -> s.commonName.is), Full(species), species = _),
      "name" -> SHtml.text(name, name = _),
//      "createdDate" -> createdDate._toForm,
      "submit" -> SHtml.submit("Add", () => {
        proj.name(name);
        //proj.species(species);
        proj.save;
        S.redirectTo("/manage", () => SelectedProject(Full(proj)));
      }))

    //proj.toForm(Full("save"), {_.save})
  }
}