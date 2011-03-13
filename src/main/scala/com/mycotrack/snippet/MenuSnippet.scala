package com.mycotrack.snippet

import xml.NodeSeq
import net.liftweb.widgets.menu.MenuStyle
import net.liftweb.widgets.menu.MenuWidget
import net.liftweb.widgets.menu.MenuWidget._

/**
 * @author chris_carrier
 * @version Oct 13, 2010
 */


class MenuSnippet {

  def render(xhtml: NodeSeq): NodeSeq = {
    MenuWidget(MenuStyle.HORIZONTAL)
  }

}