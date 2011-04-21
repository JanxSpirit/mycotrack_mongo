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
import java.awt.image.BufferedImage
import javax.imageio.ImageIO
import com.thebuzzmedia.imgscalr.Scalr
import java.io.ByteArrayOutputStream
import java.io.ByteArrayInputStream

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
      "submit" -> SHtml.submit("Save", () => {
        species.scientificName.set(scientificName)
        species.commonName.set(commonName)
        species.infoUrl.set(infoUrl)
        species.save
        S.redirectTo("/manageSpecies")
      }))

  }

  def info(xhtml: NodeSeq): NodeSeq = {
    val species = theSpecies.get.open_!

    var scientificName = species.scientificName.is
    var commonName = species.commonName.is
    var infoUrl = species.infoUrl.is

    Helpers.bind("species", xhtml,
      "scientificName" -> scientificName,
      "commonName" -> commonName,
      "infoUrl" -> infoUrl
    )
  }

  def list(xhtml: NodeSeq): NodeSeq = {
    Helpers.bind("species", xhtml,
      "allSpecies" -> {
        for (species <- Species.findAll) yield <li>
          <a href={"speciesInfo/" + species.id.toString}>
            {species.commonName.get}
          </a>
        </li>
      }
    )
  }

  def imageUpload(xhtml: Group): NodeSeq = {
    val species = theSpecies.get.open_!
    if (S.get_?) bind("ul", chooseTemplate("choose", "get", xhtml),
      "file_upload" -> fileUpload(ul => {
        theUpload(Full(ul))
        //resize image
        val orig = ImageIO.read(ul.fileStream)
        val thumbnail = Scalr.resize(Scalr.resize(orig, Scalr.Mode.FIT_TO_WIDTH, 125, 75), Scalr.Mode.FIT_TO_HEIGHT, 125, 75)
        val display = Scalr.resize(Scalr.resize(orig, Scalr.Mode.FIT_TO_WIDTH, 500, 300), Scalr.Mode.FIT_TO_HEIGHT, 500, 300)

        MycoMongoDb.gridFs(ul.fileStream) {
          fh =>
            fh.metaData.put("species_id", species.id)
            fh.metaData.put("size", "original")
            fh.filename = ul.name
        }

        val bos = new ByteArrayOutputStream
        ImageIO.write(thumbnail, "gif", bos)
        MycoMongoDb.gridFs(new ByteArrayInputStream(bos.toByteArray)) {
          fh =>
            fh.metaData.put("species_id", species.id)
            fh.metaData.put("size", "thumbnail")
            fh.filename = ul.name
        }

        val bos_disp = new ByteArrayOutputStream
        ImageIO.write(display, "gif", bos_disp)
        MycoMongoDb.gridFs(new ByteArrayInputStream(bos_disp.toByteArray)) {
          fh =>
            fh.metaData.put("species_id", species.id)
            fh.metaData.put("size", "display")
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
  }

  def speciesImage(xhtml: NodeSeq): NodeSeq = {
    val species = theSpecies.get.open_!
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

  def speciesImages(xhtml: NodeSeq): NodeSeq = {
    val species = theSpecies.get.open_!
    Helpers.bind("species", xhtml,
      "image" -> {
        println(species.id)

        val files = MycoMongoDb.gridFs.find(MongoDBObject("species_id" -> species.id)).map(_.get("filename")).distinct
        println(files)
        for (fn <- files) yield {
          println(species.id)
          println(fn)
          val thumbnail_id = MycoMongoDb.gridFs.findOne(MongoDBObject("filename" -> fn, "species_id" -> species.id, "size" -> "thumbnail")).map(_.id).getOrElse("404")
          val original_id = MycoMongoDb.gridFs.findOne(MongoDBObject("filename" -> fn, "species_id" -> species.id, "size" -> "original")).map(_.id).getOrElse("404")
          val display_id = MycoMongoDb.gridFs.findOne(MongoDBObject("filename" -> fn, "species_id" -> species.id, "size" -> "display")).map(_.id).getOrElse("404")
          val thumbnail_url = "/images/%s" format thumbnail_id
          val display_url = "/images/%s" format display_id
          <li>
			      <h3>{species.commonName.get}</h3>
			      <span>{display_url}</span>

			      <p>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aliquam ut urna. Mauris nulla. Donec nec mauris. Proin nulla dolor, bibendum et, dapibus in, euismod ut, felis.</p>
			      <a href="#"><img src={thumbnail_url} alt={species.commonName.get} /></a>
		      </li>
        }
      }
    )
  }

  private object theUpload extends RequestVar[Box[FileParamHolder]](Empty)

}

object theSpecies extends RequestVar[Box[Species]](Empty)

case class SpeciesImage(original: String, thumbnail: String, display: String)