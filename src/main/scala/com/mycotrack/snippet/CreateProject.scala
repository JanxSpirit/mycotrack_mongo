package com.mycotrack.snippet

import _root_.scala.xml.NodeSeq
import _root_.net.liftweb.util.Helpers
import net.liftweb.http.{S, SHtml}
import Helpers._
import net.liftweb.common.{Empty, Full}
import com.mycotrack.model.{Species, Project, User}
import com.mycotrack.lib.ChartGenerator
import java.util.Date

/**
 * @author chris_carrier
 * @version Oct 7, 2010
 */


class CreateProject extends ChartGenerator {
  def create(in: NodeSeq): NodeSeq =
    Helpers.bind("p", in, "form" -> "This is gonna be a form: ")

  def add(xhtml: NodeSeq): NodeSeq = {
    val proj = Project.createRecord
    var name = ""

    val tempUser = User.currentUser.open_!
    var createdDate = proj.createdDate

    Helpers.bind("entry", xhtml,
      "species" -> SHtml.select(Species.findAll.map(s => s.commonName.is -> s.commonName.is), Empty, proj.species(_)),
      "substratePreparation" -> SHtml.select(List("none" -> "none", "pasteurized" -> "pasteurized", "sterilized" -> "sterilized"), Full("none"), proj.preparation(_)),
      "container" -> SHtml.select(List("none" -> "none", "Jar - quart" -> "Jar - quart", "Jar - pint" -> "Jar - pint", "Bag - filter" -> "Bag - filter"), Full("none"), proj.container(_)),
      "substrate" -> SHtml.text("", proj.substrate(_)),
      "name" -> SHtml.text("", proj.name(_)),
      "createdDate" -> createdDate.toForm,
      "submit" -> SHtml.submit("Add", () => {
        proj.userId.set(tempUser.id)
        //proj.createdDate(new Date)
        println(proj)
        proj.save;
        createQRCode("www.google.com", Empty, Empty, proj.id)
        S.redirectTo("/manage")
      }))
  }
}