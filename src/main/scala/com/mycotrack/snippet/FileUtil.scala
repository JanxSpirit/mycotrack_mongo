/*
 * User: gregg
 * Date: 3/28/11
 * Time: 10:16 PM
 */
package com.mycotrack.snippet

import _root_.scala.xml.{NodeSeq, Text, Group}
import _root_.net.liftweb.http._
import _root_.net.liftweb.http.S
import _root_.net.liftweb.http.SHtml._
import _root_.net.liftweb.util.Helpers._
import net.liftweb.common.{Box, Empty, Full}
import com.mycotrack.db.MycoMongoDb

class FileUtil {

  // the request-local variable that hold the file parameter
  //private object theUpload extends RequestVar[Box[FileParamHolder]](Empty)

  /**
* Bind the appropriate XHTML to the form
*/
  def upload(xhtml: Group): NodeSeq =
  if (S.get_?) bind("ul", chooseTemplate("choose", "get", xhtml),
                    "file_upload" -> fileUpload(ul => MycoMongoDb.gridFs(ul.fileStream) {fh =>
                      fh.filename = ul.name
                    }))
  else bind("ul", chooseTemplate("choose", "post", xhtml)//,
     // "file_name" -> theUpload.is.map(v => Text(v.fileName)),
     // "mime_type" -> theUpload.is.map(v => Box.legacyNullTest(v.mimeType).map(Text).openOr(Text("No mime type supplied"))), // Text(v.mimeType)),
     // "length" -> theUpload.is.map(v => Text(v.file.length.toString)),
     // "md5" -> theUpload.is.map(v => Text(hexEncode(md5(v.file))))
      );


}