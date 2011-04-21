package com.mycotrack.lift

/**
 * @author chris_carrier
 * @version 4/10/11
 */

import _root_.net.liftweb.http._
import net.liftweb.common.Full


trait MycotrackAjaxRules {

  LiftRules.ajaxStart = Full(() => LiftRules.jsArtifacts.show("loading").cmd)
  LiftRules.ajaxEnd = Full(() => LiftRules.jsArtifacts.hide("loading").cmd)
}