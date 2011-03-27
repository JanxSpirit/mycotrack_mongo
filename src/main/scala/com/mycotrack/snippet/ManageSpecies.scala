/*
 * User: gregg
 * Date: 3/26/11
 * Time: 1:32 PM
 */
package com.mycotrack.snippet

import xml.{NodeSeq, Text}
import _root_.net.liftweb.util.Helpers
import Helpers._
import net.liftweb.http.{RequestVar, S, TemplateFinder, SHtml}
import com.mycotrack.model.{Species, User}
import org.bson.types.ObjectId
import net.liftweb.common.Empty
import net.liftweb.common.Box._

class ManageSpecies {

  def add(xhtml: NodeSeq): NodeSeq = {
    //val species = Species.createRecord

    val speciesb = for {
      id <- S.param("id")
      s <- Species.find(new ObjectId(id))
    } yield s

    val species = speciesb.getOrElse(Species.createRecord)

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

  def list(xhtml: NodeSeq): NodeSeq = {
    Helpers.bind("species", xhtml,
      "allSpecies" -> {
        for (species <- Species.findAll) yield <li>
          {SHtml.link("/manageSpecies?id=" + species._id.toString, () => Unit, Text(species.commonName.get))}
        </li>
      }
    )
  }
}