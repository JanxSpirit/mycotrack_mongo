package com.mycotrack.lib

import http.HttpConnection
import dispatch._
import com.mycotrack.db.MycoMongoDb
import org.bson.types.ObjectId
import net.liftweb.common.{Logger, Full, Box, Empty}


/**
 * @author chris_carrier
 * @version 3/27/11
 */


trait ChartGenerator extends HttpConnection with Logger {

  val DEFAULT_WIDTH = 250
  val DEFAULT_HEIGHT = 250
  val req = :/("chart.googleapis.com") / "chart"
  val CHART_TYPE = "qr"


  def createQRCode(ch1: String, height: Box[Int], width: Box[Int], id: ObjectId) {
    var actualWidth = DEFAULT_WIDTH
    var actualHeight = DEFAULT_HEIGHT

    (height, width) match {
      case (x: Full[Int], y: Full[Int]) => actualHeight = height.open_!; actualWidth = width.open_!;
      case (_, _) => actualHeight = DEFAULT_HEIGHT;
    }

    val dimensions = actualWidth + "x" + actualHeight
    val rquery = req <<? Map("cht" -> CHART_TYPE, "chs" -> dimensions, "ch1" -> ch1)
    info("Calling: " + rquery.to_uri)
    http(rquery >> {stream =>
      info("Calling inside: " + stream)
      MycoMongoDb.gridFs(stream) {
          fh =>
            fh.metaData.put("mapped_id", id)
            fh.filename = id.toString
        }
    })

  }

}