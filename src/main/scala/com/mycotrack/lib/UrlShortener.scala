package com.mycotrack.lib

/**
 * Based on code here: https://bitbucket.org/smerrill/lift-url-shortener
 */

import scala.StringBuilder

class IdGenerator(codeSet: String) {
  private val base = codeSet.length

  /*
   * Given an ID, return a HashCode
   */
  def getHashCode(id: Int): String = mapHashCode(id, new StringBuilder)

  private def mapHashCode(n: Int, hashCode: StringBuilder): String =
    if (n <= 0)
      hashCode.toString
    else
      mapHashCode(Math.floor(n / base).toInt, hashCode.append(codeSet.charAt(n % base)))

  /*
   * Given a hashCode, return an ID.
   */
  def getId(hashCode: String): Int = mapId(0, hashCode.toList)

  private def mapId(power: Int, hashCode: List[Char]):Int = (power, hashCode) match {
    case(power, head :: Nil)  => calcHashId(power, head)
    case(power, head :: tail) => calcHashId(power, head) + mapId(power + 1, tail)
    case(power, Nil)          => throw new Exception
  }

  private def calcHashId(power: Int, head: Char) = codeSet.indexOf(head) * Math.pow(base, power).toInt
}

trait UrlShortener {
  val defaultShortener = new IdGenerator("abcdefghijklmnopqrstuvwxyz0123456789ABCDEFGHJKLMNPQRSTUVWXYZ")
}
