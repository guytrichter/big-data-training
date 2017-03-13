package com.recongate

import akka.actor.{Actor, ActorSystem, Props}
import akka.routing.RoundRobinPool

import scala.io.Source

/**
 * @author Guy Trichter
 */
object App {
  
  case object LineRequest
  case object BeginProcessing
  case class Line(index : Integer, text : String)

  class Worker extends Actor {

    def receive = {
      case Line(index, text) => {
        println(index + ": " + text)
      }
      case _ => {
        println("Received unknown message!")
      }
    }
  }

  def main(args : Array[String]) {

    val system = ActorSystem("ReverseFile")
    val worker = system.actorOf(Props(new Worker).withRouter(RoundRobinPool(10)))

    var index : Integer = 0;
    Source.fromFile("/home/guy/backup").getLines.foreach(line => {
      worker ! Line(index, line)
      index = index + 1
    })

    system.stop(worker)
  }

}
