package com.bwater.notebook.widgets.d3

import com.bwater.notebook._, widgets._

import net.liftweb.json.JsonAST.{JValue, JArray, JInt}
import net.liftweb.json.JsonDSL._
import net.liftweb.json.DefaultFormats

case class Circles(data: Seq[Double]) extends Widget with D3 {
  private[this] val dataConnection = JSBus.createConnection
  val currentData = dataConnection biMap JsonCodec.doubleSeq

  lazy val toHtml =
    <svg width={ width.toString } height={ height.toString }
         xmlns="http://www.w3.org/2000/svg" version="1.1">
    {
      scopedScript("""
require(['observable','knockout','d3'], function(Observable, ko, d3) {
  console.log("going to make " + dataId + " observable");
  var dataO = Observable.makeObservableArray(dataId);
  dataO.subscribe(function(data) {
    var max   = d3.max(data);
    var x     = d3.scale.linear()
                  .domain([0, data.length - 1])
                  .range([0, width]);
    var y     = d3.scale.linear()
                  .domain([0, max])
                  .range([height, 0]);

    var g = d3.select(this); // our svg element

    g.selectAll('circle')
      .data(data)
    .enter().append('circle')
      .attr('cx', function(d, i) { return x(i); })
      .attr('cy', y)
      .attr('r', '2')
      .attr('fill', color);
  }, this); // `this` is the parent svg element
  dataO(dataInit);
});
""",
        ("dataId" -> dataConnection.id) ~
        ("dataInit" -> data) ~
        ("width" -> width) ~
        ("height" -> height) ~
        ("color" -> color)
      )
    } </svg>
}
