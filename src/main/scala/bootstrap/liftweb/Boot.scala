package bootstrap.liftweb

import _root_.net.liftweb.http._
import _root_.net.liftweb.sitemap._
import _root_.net.liftweb.widgets.menu.MenuWidget
import net.liftweb.common.Full
import com.mycotrack.model._
import com.mycotrack.db._
import net.liftweb.util.Helpers._
import com.mycotrack.snippet.SelectedProject
import com.mycotrack.snippet._

/**
 * A class that's instantiated early and run.  It allows the application
 * to modify lift's environment
 */
class Boot {
  def boot {
    // where to search snippet
    LiftRules.addToPackages("com.mycotrack")

    // Build SiteMap
    LiftRules.setSiteMap(SiteMap(MenuInfo.menu: _*))

    MenuWidget init

      LiftRules.ajaxStart = Full(() => LiftRules.jsArtifacts.show("loading").cmd)
      LiftRules.ajaxEnd = Full(() => LiftRules.jsArtifacts.hide("loading").cmd)

      //Set up MongoDB
      MycoMongoDb.setup

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
    }

    //REST API
    LiftRules.dispatch.append(com.mycotrack.api.MycotrackApi) // stateful — associated with a servlet container session
    LiftRules.statelessDispatchTable.append(com.mycotrack.api.MycotrackApi) // stateless — no session created

  }
}

object MenuInfo {

  import Loc._

  val IfLoggedIn = If(() => User.currentUser.isDefined, "You must be logged in")
  //val IfSuperUser = If(() => User.currentUser.get.superUser == true, "You must be a superuser")

  def menu: List[Menu] =
    List[Menu](Menu(Loc("Home", List("index"), "Home")),
      Menu(Loc("manage", List("manage"), "Manage Project", IfLoggedIn)),
      Menu(Loc("library", List("library"), "Culture Library", IfLoggedIn)),
      Menu(Loc("createCulture", List("createCulture"), "New Culture", Hidden)),
      Menu(Loc("newProject", List("create"), "New Project", Hidden)),
      Menu(Loc("createEvent", List("createEvent"), "Create Event", Hidden)),
      Menu(Loc("createNote", List("createNote"), "Create Note", Hidden)),
      Menu(Loc("events", List("events"), "Add Events", Hidden)),
      Menu(Loc("speciesInfo", List("speciesInfo"), "Species Info", Hidden)),
      Menu(Loc("manageSpecies", List("manageSpecies"), "Add Species", IfLoggedIn))) :::
      User.sitemap :::
      List[Menu](Menu("Help") / "help" / "index")

}