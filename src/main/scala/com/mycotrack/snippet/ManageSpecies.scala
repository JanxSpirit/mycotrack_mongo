/*
 * User: gregg
 * Date: 3/26/11
 * Time: 1:32 PM
 */
package com.mycotrack.snippet

import xml.NodeSeq
import _root_.net.liftweb.util.Helpers
import Helpers._
import net.liftweb.http.{RequestVar, S, TemplateFinder, SHtml}
import com.mycotrack.model.{Species, User}
import net.liftweb.common.Empty

class ManageSpecies {

  def add(xhtml: NodeSeq): NodeSeq = {
    val species = Species.createRecord
    var scientificName = species.scientificName.is
    var commonName = species.commonName.is
    var infoUrl = species.infoUrl.is

    Helpers.bind("species", xhtml,
      "scientificName" -> SHtml.text(scientificName, scientificName = _),
      "commonName" -> SHtml.text(commonName, commonName = _),
      "infoUrl" -> SHtml.text(infoUrl, infoUrl = _),
      "submit" -> SHtml.submit("Add", () => {
        species.scientificName.set(scientificName)
        species.commonName.set(commonName)
        species.infoUrl.set(infoUrl)
        species.save
        S.redirectTo("/manageSpecies")
      }))
  }
}