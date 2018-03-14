package com.frostvoid.wpMerchant.impl

import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.model.{ContentTypes, HttpEntity, StatusCodes}
import com.frostvoid.wpMerchant.util.JsonSupport

import scala.io.StdIn

/**
  * HTTP server responsible for redirecting different requests to the appropriate actors
  */
object HttpServer extends JsonSupport with MerchantService with ItemService {


  def main(args: Array[String]): Unit = {
    val routes = merchantRoutes ~ itemRoutes

    val server = Http().bindAndHandle(routes, "localhost", ServerPort)
    println(s"http server running at http://localhost:$ServerPort/")
    println("Press return to exit")
    StdIn.readLine()
    server.flatMap(_.unbind())
    system.terminate()
    println("HTTP server and akka system shut down")
  }
}