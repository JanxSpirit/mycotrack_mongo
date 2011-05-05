package com.mycotrack.snippet

import _root_.scala.xml.NodeSeq
import _root_.net.liftweb.util.Helpers
import net.liftweb.http.{S, SHtml}
import Helpers._
import net.liftweb.common.{Empty, Full}
import com.mycotrack.model.{Species, Project, User, Culture}
import com.mycotrack.db.MycoMongoDb
import java.util.Date
import net.liftweb.json.JsonDSL._
import com.mycotrack.lib.{ProjectEncodeType, ChartGenerator, UrlShortener}


/**
 * @author chris_carrier
 * @version Oct 7, 2010
 */


class CreateProject extends ChartGenerator with UrlShortener {
  def create(in: NodeSeq): NodeSeq =
    Helpers.bind("p", in, "form" -> "This is gonna be a form: ")

  def add(xhtml: NodeSeq): NodeSeq = {
    val proj = Project.createRecord
    var name = ""

    val tempUser = User.currentUser.open_!
    var createdDate = proj.createdDate
    var culture = proj.culture

    Helpers.bind("entry", xhtml,
      "culture" -> SHtml.select(Culture.findAll("userId" -> tempUser.id.toString).map(xs => xs.key.is -> xs.key.is), Empty, culture.setFromString(_)),
      "substratePreparation" -> SHtml.select(List("none" -> "none", "pasteurized" -> "pasteurized", "sterilized" -> "sterilized"), Full("none"), proj.preparation(_)),
      "container" -> SHtml.select(List("none" -> "none", "Jar - quart" -> "Jar - quart", "Jar - pint" -> "Jar - pint", "Bag - filter" -> "Bag - filter", "Tub - plastic" -> "Tub - plastic"), Full("none"), proj.container(_)),
      "randomKey" -> SHtml.checkbox(true, proj.randomKey(_), "id" -> "keyCheckbox", "onchange" -> "randomKeyToggle();"),
      "key" -> SHtml.text("", proj.key(_), "id" -> "keyText", "disabled" -> "true"),
      "substrate" -> SHtml.text("", proj.substrate(_)),
      "name" -> SHtml.text("", proj.name(_)),
      "createdDate" -> createdDate.toForm,
      "submit" -> SHtml.submit("Add", () => {
        proj.userId.set(tempUser.id)
        //proj.createdDate(new Date)

        println(proj)
        proj.save
        if (proj.randomKey.is) proj.key(defaultShortener.getHashCode(ProjectEncodeType.code ^ MycoMongoDb.incrementMasterCounter))
        proj.save
        createQRCode("www.google.com", Empty, Empty, proj.id)
        S.redirectTo("/manage")
      }))
  }
}