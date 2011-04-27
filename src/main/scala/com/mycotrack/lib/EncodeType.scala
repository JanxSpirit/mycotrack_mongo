package com.mycotrack.lib

/**
 * @author chris_carrier
 * @version 4/22/11
 */


sealed trait EncodeType { def code: Int}
case object ProjectEncodeType extends EncodeType {val code = 100000}
case object CultureEncodeType extends EncodeType {val code = 2222}