package com.mycotrack.snippet

import xml.{Text, NodeSeq}
import com.mycotrack.model.{Project, Species}
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

  def showAddItem(node: NodeSeq): NodeSeq = {
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
  }

  def showEditItem(node: NodeSeq): NodeSeq = {
    if (SelectedProject.get != Empty) {
      var id = SelectedProject.get.open_!.id
      val project = Project.find(id).open_!

      var name = project.name.is
      var species = project.species.is

      val template = TemplateFinder.findAnyTemplate(ITEM_TEMPLATE :: Nil).openOr(<p></p>)
      val content = bind("projectForm", template, "title" -> Text("Edit item"),
        "name" -> SHtml.text(name, name = _),
        "species" -> SHtml.hidden(() => {
          species = species
        }),
        "submit" -> SHtml.submit("save", () => saveItem(id, name)),
        "close" -> SHtml.link("index", () => clear, Text("close")))

      <div>{content}</div>
    }
    else {
      <div></div>
    }
  }

  def list(node: NodeSeq): NodeSeq = {
    Project.findAll match {
      case Nil => Text("There is no items in database")
      case projects => projects.flatMap(i => bind("project", node, "name" -> getEditLink(i),
        "species" -> speciesLink(i.species.is),
//        "createdDate" -> {
//          i.createdDate
//        },
        "remove" -> getRemoveLink(i)))
    }
  }

  def addLink(node: NodeSeq): NodeSeq = {
    SHtml.link("create", () => {}, Text("New Project"))
  }

  def speciesLink(commonName: String): NodeSeq = {
    val species = Species.find("commonName" -> "shiitake").open_!
    val url = "http://" + species.infoUrl.is
    info("Got species link: " + url)

    <span><a href={url}>{species.commonName.is}</a></span>
  }

  private def addItem(name: String): Any = {
    val project = new Project
    project.name(name)

    saveItem(project)
  }

  private def saveItem(id: ObjectId, name: String): Any = {
    val item = Project.find(id).open_!
    item.name(name)

    saveItem(item)
  }

  private def saveItem(project: Project): Any = {
    project.save
  }

  private def getEditLink(project: Project): NodeSeq = {
    SHtml.link("events", () => {SelectedProject(Full(project)); info("SelectProject is: " + SelectedProject.is.open_!);}, Text(project.name.is))
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