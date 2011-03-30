package bootstrap.liftweb

import _root_.net.liftweb.http._
import _root_.net.liftweb.sitemap._
import java.sql.{Connection, DriverManager}
import net.liftweb.mapper._
import _root_.net.liftweb.widgets.menu.MenuWidget
import net.liftweb.common.{Logger, Empty, Full, Box}
import com.mycotrack.model._
import com.mycotrack.db._

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

    //Set up MongoDB
    MycoMongoDb.setup

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
      Menu(Loc("newProject", List("create"), "New Project", IfLoggedIn)),
      Menu(Loc("createEvent", List("createEvent"), "Create Event", Hidden)),
      Menu(Loc("createNote", List("createNote"), "Create Note", Hidden)),
      Menu(Loc("events", List("events"), "Add Events", Hidden)),
      Menu(Loc("manageSpecies", List("manageSpecies"), "Add Species", IfLoggedIn))) :::
      User.sitemap :::
      List[Menu](Menu("Help") / "help" / "index")

}