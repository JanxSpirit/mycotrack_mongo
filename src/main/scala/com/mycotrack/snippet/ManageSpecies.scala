/*
 * User: gregg
 * Date: 3/26/11
 * Time: 1:32 PM
 */
package com.mycotrack.snippet

import _root_.net.liftweb.util.Helpers
import Helpers._
import net.liftweb.http.SHtml._
import com.mycotrack.model.{Species, User}
import org.bson.types.ObjectId
import net.liftweb.common.Box._
import xml.{Group, NodeSeq, Text}
import com.mycotrack.db.MycoMongoDb
import net.liftweb.http._
import net.liftweb.common.{Full, Box, Empty}
import com.mongodb.casbah.Imports._

class ManageSpecies {

  val speciesb = for {
    id <- S.param("id")
    s <- Species.find(new ObjectId(id))
  } yield s

  val species = speciesb.getOrElse(Species.createRecord)
  theSpecies(Full(species))

  def add(xhtml: NodeSeq): NodeSeq = {
    //val species = Species.createRecord

    var scientificName = species.scientificName.is
    var commonName = species.commonName.is
    var infoUrl = species.infoUrl.is

    Helpers.bind("species", xhtml,
      "scientificName" -> SHtml.text(scientificName, scientificName = _),
      "commonName" -> SHtml.text(commonName, commonName = _),
      "infoUrl" -> SHtml.text(infoUrl, infoUrl = _),
      "submit" -> SHtml.submit("Save", () => {
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

  def imageUpload(xhtml: Group): NodeSeq =
    if (S.get_?) bind("ul", chooseTemplate("choose", "get", xhtml),
      "file_upload" -> fileUpload(ul => {
        theUpload(Full(ul))
        MycoMongoDb.gridFs(ul.fileStream) {
          fh =>
            fh.metaData.put("species_id", species.id)
            fh.filename = ul.name
        }
      }))
    else bind("ul", chooseTemplate("choose", "post", xhtml),
      "scientific_name" -> Text(species.scientificName.get),
      "common_name" -> Text(species.commonName.get),
      "file_name" -> theUpload.is.map(v => Text(v.fileName)),
      "mime_type" -> theUpload.is.map(v => Box.legacyNullTest(v.mimeType).map(Text).openOr(Text("No mime type supplied"))), // Text(v.mimeType)),
      "length" -> theUpload.is.map(v => Text(v.file.length.toString)),
      "md5" -> theUpload.is.map(v => Text(hexEncode(md5(v.file))))
    );

  def speciesImage(xhtml: NodeSeq): NodeSeq = {
    Helpers.bind("species", xhtml,
      "image" -> {
        val photo = MycoMongoDb.gridFs.findOne(MongoDBObject("species_id" -> species.id))
        photo match {
          case None => Text("No photo found")
          case Some(photo) => {
            val url = "/images/%s" format photo.id
            <img src={url}/>
          }
        }
      }
    )
  }

  private object theUpload extends RequestVar[Box[FileParamHolder]](Empty)

  private object theSpecies extends RequestVar[Box[Species]](Empty)
}