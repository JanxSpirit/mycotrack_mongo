package com.mycotrack.snippet

import _root_.scala.xml.NodeSeq
import _root_.net.liftweb.util.Helpers
import net.liftweb.http.{S, SHtml}
import Helpers._
import net.liftweb.common.{Empty, Full}
import com.mycotrack.model.{Species, Project, User}
import com.mycotrack.lib.ChartGenerator

/**
 * @author chris_carrier
 * @version Oct 7, 2010
 */


class CreateProject extends ChartGenerator {
  def create(in: NodeSeq): NodeSeq =
    Helpers.bind("p", in, "form" -> "This is gonna be a form: ")

  //When you want to use QR codes try the google API like:
  //https://chart.googleapis.com/chart?cht=qr&chs=250x250&ch1=http://www.mycotrack.com

  def add(xhtml: NodeSeq): NodeSeq = {
    val proj = Project.createRecord
    var name = ""

    val tempUser = User.currentUser.open_!
    var createdDate = proj.createdDate

    Helpers.bind("entry", xhtml,
      "species" -> SHtml.select(Species.findAll.map(s => s.commonName.is -> s.commonName.is), Empty, proj.species(_)),
      "name" -> SHtml.text(name, name = _),
      "createdDate" -> createdDate.toForm,
      "submit" -> SHtml.submit("Add", () => {
        proj.name.set(name)
        proj.userId.set(tempUser.id)
        proj.createdDate.set(createdDate.get)
        println(proj)
        proj.save;
        S.redirectTo("/manage")
      }))
  }
}