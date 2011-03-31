package com.mycotrack.lib

import http.HttpConnection
import net.liftweb.common.{Full, Box, Empty}
import dispatch._

/**
 * @author chris_carrier
 * @version 3/27/11
 */


trait ChartGenerator extends HttpConnection {

  val DEFAULT_WIDTH = 250
  val DEFAULT_HEIGHT = 250
  val req = :/("chart.googleapis.com") / "chart"
  val CHART_TYPE = "qr"


  def createQRCode(ch1: String, height: Box[Int], width: Box[Int]) {
    var actualWidth = DEFAULT_WIDTH
    var actualHeight = DEFAULT_HEIGHT

    (height, width) match {
      case (x: Full[Int], y: Full[Int]) => actualHeight = height.open_!; actualWidth = width.open_!;
      case (_, _) => actualHeight = DEFAULT_HEIGHT;
    }

    val dimensions = actualWidth + "x" + actualHeight
    val rquery = req <<? Map("cht" -> CHART_TYPE, "chs" -> dimensions, "ch1" -> "http://www.mycotrack.com") >>> System.out

  }

}