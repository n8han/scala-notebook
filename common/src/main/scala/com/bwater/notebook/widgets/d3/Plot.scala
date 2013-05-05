package com.bwater.notebook.widgets.d3

import com.bwater.notebook._, widgets._

import net.liftweb.json.JsonAST.{JValue, JArray, JInt}
import net.liftweb.json.JsonDSL._
import net.liftweb.json.DefaultFormats


case class Plot(data: Seq[(Double,Double)]) extends Widget with D3 {
  private[this] val dataConnection = JSBus.createConnection
  val currentData = dataConnection biMap JsonCodec.pairSeq

  lazy val toHtml =
    <svg width={ width.toString } height={ height.toString }
         xmlns="http://www.w3.org/2000/svg" version="1.1">
    {
      scopedScript(
        "require('js/plot', function(f) { f.call(data, this); });",
        ("dataId" -> dataConnection.id) ~
        ("dataInit" -> JsonCodec.pairSeq.decode(data)) ~
        ("width" -> width) ~
        ("height" -> height) ~
        ("color" -> color)
      )
    } </svg>
}
