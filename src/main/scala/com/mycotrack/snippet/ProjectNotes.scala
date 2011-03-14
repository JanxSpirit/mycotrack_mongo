package com.mycotrack.snippet

import xml.{Text, NodeSeq}
import net.liftweb.http._
import net.liftweb.common.Empty
import xml.NodeSeq
import com.mycotrack.model.{Note, Project}
import net.liftweb.common.{Full, Empty, Box}
import net.liftweb.util._
import Helpers._

/**
 * @author chris_carrier
 * @version Nov 9, 2010
 */


class ProjectNotes {

  private object SelectedProject extends RequestVar[Box[Project]](Empty)

  def list(node: NodeSeq): NodeSeq = {
    val project = SelectedProject.is.open_!
    //SelectedProject(project.open_!)

    project.notes.get match {
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
    val project = SelectedProject.is.open_!

    SHtml.link("createEvent", () => {SelectedProject(Full(project))}, Text("new event"))
  }

  private def getEditLink(project: Project): NodeSeq = {
        SHtml.link("events", () => {}, Text("edit"))
    }

    private def getRemoveLink(note: Note, project: Project): NodeSeq = {
        SHtml.link("events", () => {note.delete_!; SelectedProject(Project.find(project.id));}, Text("delete"))
    }

  def add(xhtml: NodeSeq): NodeSeq = {
    val project = SelectedProject.is.open_!
    var notes: List[Note] = project.notes.is
    var name: String = ""
    var body: String = ""

    bind("note", xhtml,
      //"species" -> SHtml.select(Species.findAll.map(s => s.commonName.is -> s.commonName.is), Full(species), species = _),
      "name" -> SHtml.text(name, name = _),
      "body" -> SHtml.text(body, body = _),
//      "createdDate" -> createdDate._toForm,
      "submit" -> SHtml.submit("Add", () => {
        val note = new Note
        note.name(name)
        note.body(body)

        notes :+ note
        project.save;
        S.redirectTo("/manage", () => SelectedProject(Full(project)));
      }))

    //proj.toForm(Full("save"), {_.save})
  }

}