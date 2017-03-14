package com.recongate

import akka.actor.{Actor, ActorSystem, Props}
import akka.routing.RoundRobinPool
import com.redis.{RedisClient, RedisClientPool, RedisClientPoolByAddress}

import scala.io.Source

/**
 * @author Guy Trichter
 */
object App {

  case class Line(index : Integer, text : String)


  class Worker() extends Actor {

    val redisClient : RedisClient = new RedisClient("localhost", 6379)

    def receive = {
      case Line(index, text) => {
        val reverseLine = text.reverse
        redisClient.hset("reverseLines", index, reverseLine)
        reverseLine.split(" ").foreach(word => {
          val currentScore : Option[Double] = redisClient.zscore("popularWords", word)
          val newScore = currentScore.getOrElse(0.0) + 1.0
          println(word + ": " + newScore)
          redisClient.zadd("popularWords", newScore, word)
        })
      }
      case _ => {
        println("Received unknown message!")
      }
    }
  }

  def main(args : Array[String]) {

    val system = ActorSystem("ReverseFile")
    val worker = system.actorOf(Props(new Worker()).withRouter(RoundRobinPool(10)))

    var index : Integer = 0
    Source.fromFile("/home/guy/backup").getLines.foreach(line => {
      worker ! Line(index, line)
      index += 1
    })

//    system.terminate
  }

}
