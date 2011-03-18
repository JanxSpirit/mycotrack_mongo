package com.mycotrack.snippet

import xml.{Text, NodeSeq}
import net.liftweb.http.{RequestVar, S, TemplateFinder, SHtml}
import net.liftweb.util.{Helpers, Log}
import Helpers._
import com.mycotrack.model.{Event, Project}
import net.liftweb.common.{Logger, Full, Empty, Box}

/**
 * @author chris_carrier
 * @version Nov 9, 2010
 */


class ProjectEvents extends Logger {

  //private object SelectedProject extends RequestVar[Project](Empty)
  /*
  def list(node: NodeSeq): NodeSeq = {
    val project = SelectedProject.is.open_!
    //SelectedProject(project.open_!)

    project.events match {
            case Seq() => Text("There are no events")
            case e => e.flatMap(e => bind("event", node, "name" -> {e.eventType},
                                                                "edit" -> getEditLink(project),
                                                                "remove" -> getRemoveLink(e, project)))
        }

  }*/

  def projectName(node: NodeSeq): NodeSeq = {
    /*info("SelectProject is full?: " + SelectedProject.is)
    val project = SelectedProject.is.open_!

    <span>Project: {project.name}</span>*/
    <span></span>
  }

  /*def addEvent(node: NodeSeq): NodeSeq = {
    val project = SelectedProject.is.open_!

    SHtml.link("createEvent", () => {SelectedProject(Full(project))}, Text("new event"))
  }

  private def getEditLink(project: Project): NodeSeq = {
        SHtml.link("events", () => {}, Text("edit"))
    }

    private def getRemoveLink(event: Event, project: Project): NodeSeq = {
        SHtml.link("events", () => {event.delete_!; SelectedProject(Project.find(project.id));}, Text("delete"))
    }
    */
}