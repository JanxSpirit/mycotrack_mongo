package com.mycotrack.lift

/*
 * User: gregg
 * Date: 4/9/11
 * Time: 11:35 PM
 */
import _root_.net.liftweb.http._
import net.liftweb.common.Full
import com.mycotrack.model._
import com.mycotrack.snippet._

trait MycotrackUrlRewriter {

  LiftRules.statelessRewrite.append {
    case RewriteRequest(
    ParsePath("projects" :: id :: Nil, "", true, false), _, _) => {
      val project = Project.find(id)
      SelectedProject(project)
      RewriteResponse("events" :: Nil)
    }
  }.append {
    case RewriteRequest(
    ParsePath("manageSpecies" :: Nil, "", true, false), _, _) => {
      val species = Species.createRecord
      theSpecies(Full(species))
      RewriteResponse(ParsePath("manageSpecies" :: Nil, "", true, false), Map.empty, true)
    }
  }.append {
    case RewriteRequest(
    ParsePath("speciesInfo" :: id :: Nil, "", true, false), _, _) => {
      val species = Species.find(id)
      theSpecies(species)
      RewriteResponse(ParsePath("speciesInfo" :: Nil, "", true, false), Map.empty, true)
    }
  }.append {
    case RewriteRequest(
    ParsePath("splitProject" :: id :: Nil, "", true, false), _, _) => {
      val project = Project.find(id)
      SelectedProject(project)
      RewriteResponse(ParsePath("splitProject" :: Nil, "", true, false), Map.empty, true)
    }
  }

}