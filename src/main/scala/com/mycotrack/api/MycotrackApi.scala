/*
 * User: gregg
 * Date: 3/27/11
 * Time: 2:41 PM
 */
package com.mycotrack.api

import net.liftweb.http._
import net.liftweb.http.rest._
import org.bson.types.ObjectId
import com.mongodb.BasicDBObject
import com.mycotrack.db.MycoMongoDb
import com.mongodb.casbah.gridfs.GridFSDBFile
import java.io.{InputStream, ByteArrayOutputStream}

object MycotrackApi extends RestHelper {

  serve {
    case Req("hello" :: msg :: Nil, _, GetRequest) => PlainTextResponse(msg)
    case Req("images" :: id :: Nil, _, GetRequest) => getImage(id)
  }

  def getImage(id: String): LiftResponse = {
    val photo = getPhoto(id)
    println(photo)
    photo match {
      case None => NotFoundResponse("photo not found")
      case Some(photo) => {
        val bytes: Array[Byte] = photo.inputStream
        val headers = List {
          ("Content-Type", "image/jpeg")
        }
        InMemoryResponse(bytes, headers, Nil, 200)
        //streaming seems preferrable but didn't immediately work
        //StreamingResponse(photo.inputStream, () => Unit, photo.size, headers, Nil, 200)
      }
    }
  }

  private def getPhoto(id: String): Option[GridFSDBFile] = {
    val query = new BasicDBObject
    query.put("_id", new ObjectId(id))
    MycoMongoDb.gridFs.findOne(query)
  }

  implicit def inputStream2Bytes(in: InputStream): Array[Byte] = {
    val out: ByteArrayOutputStream = new ByteArrayOutputStream(1024)
    val buffer: Array[Byte] = new Array[Byte](1024)
    var len = 1
    while (len >= 0) {
      len = in.read(buffer)
      if (len > 0) {
        out.write(buffer, 0, len)
      }
    }
    in.close
    out.close
    out.toByteArray
  }


}