package com.mycotrack.snippet

import _root_.scala.xml.NodeSeq
import _root_.net.liftweb.util.Helpers
import net.liftweb.http.{S, SHtml}
import Helpers._
import com.mycotrack.model.{Event, Project}
import net.liftweb.common.{Full, Empty}

/**
 * @author chris_carrier
 * @version Nov 23, 2010
 */


class CreateEvent {

  def add(xhtml: NodeSeq): NodeSeq = {
    val project = SelectedProject.is.open_!
    var eventType = ""

    Helpers.bind("event", xhtml,
      "eventType" -> SHtml.select(List(("test1", "test1"), ("test2", "test2")), Empty, eventType = _),
      "submit" -> SHtml.submit("Add", () => {
//        val event = Event.save
//        event.eventType(eventType)
//        project.events += event
        project.save
        S.redirectTo("/events", () => SelectedProject(Full(project)))
      }))

    //proj.toForm(Full("save"), {_.save})
  }
}