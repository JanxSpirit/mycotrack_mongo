package com.mycotrack.model

/**
 * @author chris_carrier
 * @version 4/10/11
 */


object PreparationMethod extends Enumeration {
  val Pasteurized = Value("Pasteurized")
  val Sterilized = Value("Sterilized")
  val None = Value("None")
}