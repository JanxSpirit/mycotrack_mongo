package com.mycotrack.snippet

import xml.{Text, NodeSeq}
import net.liftweb.http.{RequestVar, S, TemplateFinder, SHtml}
import net.liftweb.util.{Helpers, Log}
import Helpers._
import net.liftweb.common.{Logger, Full, Empty, Box}
import com.mycotrack.model.{Note, Event, Project}

/**
 * @author chris_carrier
 * @version Nov 9, 2010
 */


class ProjectNotes extends Logger {

  def list(node: NodeSeq): NodeSeq = {
    val project = SelectedProject.is.open_!
    //SelectedProject(project.open_!)

    project.notes.get match {
        case Seq() => Text("There are no notes")
            case n => n.flatMap(i => bind("note", node, "name" -> {i.name},
                                                        "body" -> {i.body},
                                                                "edit" -> getEditLink(project),
                                                                "remove" -> getRemoveLink(i, project)))
            //case _ => Text("There are no notes")
        }

  }

  def projectName(node: NodeSeq): NodeSeq = {
    val project = SelectedProject.is.open_!

    <span>Project: {project.name}</span>
  }

  def addNote(node: NodeSeq): NodeSeq = {
    info("SelectProject in notes: " + SelectedProject.is)
    val project = SelectedProject.is.open_!

    SHtml.link("createNote", () => {SelectedProject(Full(project))}, Text("new note"))
  }

  private def getEditLink(project: Project): NodeSeq = {
        SHtml.link("events", () => {}, Text("edit"))
    }

    private def getRemoveLink(note: Note, project: Project): NodeSeq = {
        SHtml.link("events", () => {SelectedProject(Project.find(project.id));}, Text("delete"))
    }

  def add(xhtml: NodeSeq): NodeSeq = {
    val project = SelectedProject.is.open_!
    val notes: List[Note] = project.notes.is
    var name: String = ""
    var body: String = ""

    bind("note", xhtml,
      //"species" -> SHtml.select(Species.findAll.map(s => s.commonName.is -> s.commonName.is), Full(species), species = _),
      "name" -> SHtml.text(name, name = _),
      "body" -> SHtml.text(body, body = _),
//      "createdDate" -> createdDate._toForm,
      "submit" -> SHtml.submit("Add", () => {
        val note = new Note(name, body)

        val newList = note :: notes
        project.notes(newList)
        project.save;
        S.redirectTo("/events", () => SelectedProject(Full(project)));
      }))

    //proj.toForm(Full("save"), {_.save})
  }

}