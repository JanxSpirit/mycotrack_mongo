package com.mycotrack.model

import net.liftweb.mapper._
import net.liftweb.common._
import net.liftweb.http.S
import xml.{Node, NodeSeq, Text}
import java.util.Date
import java.text.SimpleDateFormat

/**
 * @author chris_carrier
 * @version Nov 28, 2010
 */


class MyMappedDate[T <: Mapper[T]](owner: T) extends MappedDateTime[T](owner) {

  /**
   * When _toForm is called, the class attribute's value will be set
  to this.
   * Simply override this with a different string to change the value.
   *
   * The default value is: <code>datepicker</code> ie:
  class="datepicker"
   */
  def classValue = "datepicker"

  /**
   * Create an input field for the item that uses the datepicker class
   */
  override def _toForm: Box[NodeSeq] =
    S.fmapFunc({
      s: List[String] => {this.set(parseDate(s(0)))} // call when submitted
    }) {
      funcName =>
        Full(<input type='text' class={classValue} id={fieldId}
                    name={funcName} lift:gc={funcName}
                    value={is match {
                      case null => printDate(new Date)
                      case s =>
                        printDate(s)
                    }}/>)
    }

  override def asHtml: Node = Text(is match {
    case null => ""
    case s =>
      printDate(s)
  })

  /**@return a SimpleDateFormat in the format "MM/dd/yyyy" **/
  val printDateFormatter = new SimpleDateFormat("MM/dd/yyyy")

  /**@return a date formatted with the date format */
  def printDate(in: Date): String = printDateFormatter.format(in)

  /**@return a date formatted with the date format (from a number of
  millis) */
  def printDate(in: Long): String = printDateFormatter.format(new Date(in))

  /**@return a date formatted with the date format (from a number of
  millis) */
  def parseDate(dateString: String): Date = printDateFormatter.parse(dateString)
}