package com.mycotrack.snippet

import _root_.scala.xml.NodeSeq
import _root_.net.liftweb.util.Helpers
import net.liftweb.http.{S, SHtml}
import Helpers._
import net.liftweb.common.{Empty, Full}
import com.mycotrack.model.{Species, Project, User}

/**
 * @author chris_carrier
 * @version Oct 7, 2010
 */


class CreateProject {
  def create(in: NodeSeq): NodeSeq =
    Helpers.bind("p", in, "form" -> "This is gonna be a form: ")

  def add(xhtml: NodeSeq): NodeSeq = {
    val proj = Project.createRecord
    var name = proj.name.is
    val tempUser = User.currentUser.open_!
    //var species: Species = proj.species.obj.open_!
    var createdDate = proj.createdDate

    Helpers.bind("entry", xhtml,
      "species" -> SHtml.select(Species.findAll.map(s => s.commonName.is -> s.commonName.is), Empty, proj.species(_)),
      "name" -> SHtml.text(name, name = _),
      //"species" -> SHtml.text(species, species = _),
      "createdDate" -> createdDate.toForm,
      "submit" -> SHtml.submit("Add", () => {
        proj.name.set(name)
        proj.userId.set(tempUser.id)
        proj.createdDate.set(createdDate.get)
        //proj.species(species);
        println(proj)
        proj.save;
        S.redirectTo("/manage")
      }))

    //proj.toForm(Full("save"), {_.save})
  }
}