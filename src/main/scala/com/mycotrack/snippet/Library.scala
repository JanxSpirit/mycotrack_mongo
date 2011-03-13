package com.mycotrack.snippet

import xml.{Text, NodeSeq}
import net.liftweb.http.{RequestVar, S, TemplateFinder, SHtml}
import net.liftweb.util.{Helpers, Log}
import Helpers._
import net.liftweb.common.{Full, Empty, Box}
import com.mycotrack.model.{Species, Culture, Project, User}

/**
 * @author chris_carrier
 * @version Nov 30, 2010
 */


class Library {

 /* def list(node: NodeSeq): NodeSeq = {
    User.currentUser.get.cultures match {
            //case Nil => Text("")
            case cultures => cultures.flatMap(c => bind("culture", node, "type" -> {c.cultureType.obj.open_!.name},
                                                                "cultureType" -> {c.cultureType.obj.open_!.name},
                                                                "species" -> {c.species.obj.open_!.commonName},
                                                                "createdDate" -> {c.createdDate},
                                                                "remove" -> getRemoveLink(c)))
        }
  }*/

  def addLink(node: NodeSeq): NodeSeq = {
    SHtml.link("createCulture", () => {}, Text("New Culture"))
  }

  def add(xhtml: NodeSeq): NodeSeq = {
    val culture = new Culture
//    var cultureType = culture.cultureType.obj
//    var species = culture.species.obj
    var createdDate = culture.createdDate

    Helpers.bind("culture", xhtml,
//      "species" -> SHtml.selectObj[Species](Species.findAll.map(s => (s, s.commonName.is)), Empty, culture.species(_)),
//      "cultureType" -> SHtml.selectObj[CultureType](CultureType.findAll.map(ct => (ct, ct.name.is)), Empty, culture.cultureType(_)),
//      "createdDate" -> createdDate._toForm,
      "submit" -> SHtml.submit("Add", () => {
//        var userCultures = User.currentUser.get.cultures;
//        userCultures += culture;
//        User.currentUser.get.cultures.save
        S.redirectTo("/library");
      }))

    //proj.toForm(Full("save"), {_.save})
  }

  private def getEditLink(culture: Culture): NodeSeq = {
        SHtml.link("createCulture", () => {SelectedCulture(Full(culture))}, Text("edit"))
    }

    private def getRemoveLink(culture: Culture): NodeSeq = {
        SHtml.link("library", removeCulture(culture), Text("delete"))
    }

  private def removeCulture(culture: Culture): () => Any = {
        () => {
            try {
                //val project = Project.find(id).open_!
                culture.delete_!

                S.notice("Item deleted")
            }
            catch {
                case e: Exception => {
                    S.error(e.getMessage)
                }
            }
        }
    }
}

object SelectedCulture extends RequestVar[Box[Culture]](Empty)