package com.mycotrack.snippet

import xml.{Text, NodeSeq}
import com.mycotrack.model.{Project, Species, User}
import net.liftweb.http.{RequestVar, S, TemplateFinder, SHtml}
import net.liftweb.util.{Helpers, Log}
import Helpers._
import net.liftweb.common.{Full, Empty, Box, Logger}
import org.bson.types.ObjectId
import com.mongodb._
import com.mongodb.casbah.Imports._
import net.liftweb.json.JsonDSL._

/**
 * @author chris_carrier
 * @version Oct 18, 2010
 */


class ProjectItems extends Logger {

  private val ITEM_TEMPLATE = "templates-hidden/project_item"

  private object ShowAddProject extends RequestVar[Int](0)

  def clear = {
    ShowAddProject(0)
    SelectedProject(Empty)
  }

  /*def showAddItem(node: NodeSeq): NodeSeq = {
    ShowAddProject.get match {
      case 1 => {
        var name = ""
        val template: NodeSeq = TemplateFinder.findAnyTemplate(ITEM_TEMPLATE :: Nil).openOr(<p></p>)
        val content = bind("projectForm", template, "title" -> Text("New project"),
          "name" -> SHtml.text(name, name = _),
          "id" -> Text(""),
          "submit" -> SHtml.submit("save", () => addItem(name)),
          "close" -> SHtml.link("index", () => clear, Text("close")))

        <div>{content}</div>
      }
      case _ => SHtml.link("create", () => ShowAddProject(1), <p>Add</p>)
    }
  }*/

  def showEditItem(node: NodeSeq): NodeSeq = {
    if (SelectedProject.get != Empty) {
      var id = SelectedProject.get.open_!.id
      val project = Project.find(id).open_!

      var key = project.key.is
      var substrate = project.substrate.is

      val template = TemplateFinder.findAnyTemplate(ITEM_TEMPLATE :: Nil).openOr(<p></p>)
      val content = bind("projectForm", template, "title" -> Text("Edit item"),
        "key" -> SHtml.text(key, key = _),
        "substrate" -> SHtml.hidden(() => {substrate = substrate}),
        "submit" -> SHtml.submit("save", () => project.save),
        "close" -> SHtml.link("index", () => clear, Text("close")))

      <div>{content}</div>
    }
    else {
      <div></div>
    }
  }

  def list(node: NodeSeq): NodeSeq = {
    val currentUser = User.currentUser.open_!

    val projects = Project.findAll("userId" -> currentUser.id.toString)

    Project.findAll("userId" -> currentUser.id.toString) match {
      case Nil => Text("There is no items in database")
      case projects => projects.flatMap(i => bind("project", node, "key" -> getEditLink(i),
        "key" -> getEditLink(i),
        "species" -> speciesLink(i),
        "substrate" -> i.substrate.is,
//        "createdDate" -> {
//          i.createdDate
//        },
        "remove" -> getRemoveLink(i)))
    }
  }

  def addLink(node: NodeSeq): NodeSeq = {
    SHtml.link("create", () => {}, Text("New Project"))
  }

  def speciesLink(project: Project): NodeSeq = {
    /*val culture = project.culture.obj.openOr(project.culture.defaultValue)
    val species = culture.species.obj.openOr(culture.species.defaultvalue)

    val url = "http://" + species.infoUrl.is
    info("Got species link: " + url)

    <span><a href={url}>{species.commonName.is}</a></span>*/
    <span><a href='blah'>www.google.com</a></span>
  }

  private def getEditLink(project: Project): NodeSeq = {
    <a href={"projects/" + project.key.is}>{project.key.is}</a>
  }

  private def getAddEventLink(project: Project): NodeSeq = {
    SHtml.link("events", () => {
      SelectedProject(Full(project))
    }, Text(project.name.displayName))
  }

  private def getRemoveLink(project: Project): NodeSeq = {
    SHtml.link("create", removeProject(project), Text("delete"))
  }

  private def removeProject(project: Project): () => Any = {
    () => {
      try {
        //val project = Project.find(id).open_!
        project.delete_!

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

object SelectedProject extends RequestVar[Box[Project]](Empty)