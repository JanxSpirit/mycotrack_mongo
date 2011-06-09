package com.mycotrack.snippet

import xml.{Text, NodeSeq}
import net.liftweb.http.{RequestVar, S, TemplateFinder, SHtml}
import net.liftweb.util.{Helpers, Log}
import Helpers._
import com.mycotrack.model.{Species, Culture, Project, User}
import net.liftweb.common._
import com.mongodb._
import com.mongodb.casbah.Imports._
import com.mongodb.casbah.Implicits._
import net.liftweb.json.JsonDSL._
import net.liftweb.record.field._
import com.mycotrack.db.MycoMongoDb
import com.mycotrack.lib.{CultureEncodeType, ChartGenerator, UrlShortener}

/**
 * @author chris_carrier
 * @version Nov 30, 2010
 */


class Library extends Logger with UrlShortener {

  def list(node: NodeSeq): NodeSeq = {
    Culture.findAll("userId" -> User.currentUser.open_!.id.toString) match {
            case Nil => Text("No cultures.")
            case cultures => cultures.flatMap(c => bind("culture", node,
                                                                "cultureType" -> {c.cultureType},
                                                                "key" -> c.key,
                                                                "species" -> {c.species.obj.open_!.commonName},
                                                                "createdDate" -> {c.createdDate.is.getTime.toString},
                                                                "remove" -> getRemoveLink(c)))
        }
  }

  def addLink(node: NodeSeq): NodeSeq = {
    SHtml.link("createCulture", () => {}, Text("New Culture"))
  }

  def add(xhtml: NodeSeq): NodeSeq = {
    val culture = Culture.createRecord
    val user = User.currentUser.open_!
    var createdDate = culture.createdDate

    Helpers.bind("culture", xhtml,
      "species" -> SHtml.select(Species.findAll.map(s => s.id.toString -> s.commonName.is), Empty, culture.species.setFromString(_)),
      "cultureType" -> SHtml.text("", culture.cultureType(_)),
      "randomKey" -> SHtml.checkbox(true, culture.randomKey(_), "id" -> "keyCheckbox", "onchange" -> "randomKeyToggle();"),
      "key" -> SHtml.text("", culture.key(_), "id" -> "keyText", "disabled" -> "true"),
      "createdDate" -> createdDate.toForm,
      "submit" -> SHtml.submit("Add", () => {
        culture.userId(user.id)
        if (culture.randomKey.is) culture.key(defaultShortener.getHashCode(CultureEncodeType.code ^ MycoMongoDb.incrementMasterCounter))
        culture.save
        val userCultures: List[ObjectId] = user.cultureList.value
        user.cultureList.set(culture.id :: userCultures)
        user.save
        S.redirectTo("/library");
      }))
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