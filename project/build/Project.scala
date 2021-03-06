import sbt._

class LiftProject(info: ProjectInfo) extends DefaultWebProject(info) {
  val liftVersion = "2.3"

  // uncomment the following if you want to use the snapshot repo
  //val scalatoolsSnapshot = ScalaToolsSnapshots

  // If you're using JRebel for Lift development, uncomment
  // this line
  // override def scanDirectories = Nil

  val buzzrepo = "The Buzz Media Maven Repository" at "http://maven.thebuzzmedia.com"

  val liftwebkit = "net.liftweb" %% "lift-webkit" % liftVersion % "compile->default"
  val liftmongodb = "net.liftweb" %% "lift-mongodb" % liftVersion % "compile->default"
  val liftmongodbrecord = "net.liftweb" %% "lift-mongodb-record" % liftVersion % "compile->default"
  val liftwidgets = "net.liftweb" %% "lift-widgets" % liftVersion % "compile->default"
  val liftwizard = "net.liftweb" %% "lift-wizard" % liftVersion % "compile->default"
  val logback = "ch.qos.logback" % "logback-classic" % "0.9.28" % "compile->default"

  //casbah
  val casbah = "com.mongodb.casbah" %% "casbah" % "2.1.2"

  //rogue mongo DSL
  //val rogue = "com.foursquare" % "rogue_2.8.0" % "1.0.3" % "compile->default"

  // jetty
  val jetty6 = "org.mortbay.jetty" % "jetty" % "6.1.22" % "test"
  val servletApi = "org.mortbay.jetty" % "servlet-api" % "2.5-20081211" % "provided"

  //dispatch
  val dispatch = "net.databinder" %% "dispatch-http" % "0.7.8"

  //imgscalr
  val imgscalr = "com.thebuzzmedia" % "imgscalr-lib" % "3.1"

  //lazy val jetty = "org.mortbay.jetty" % "jetty" % "6.1.22" % "test->default"
  //"junit" % "junit" % "4.5" % "test->default",
  //"org.scala-tools.testing" %% "specs" % "1.6.7.2" % "test->default",
}
