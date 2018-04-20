package com.frostvoid.wpMerchant.impl

import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import com.frostvoid.wpMerchant.services.{ItemService, MerchantService}
import com.frostvoid.wpMerchant.util.AkkaSupport

import scala.io.StdIn

/**
  * HTTP server responsible for redirecting different requests to the appropriate services
  */
object HttpServer extends AkkaSupport {
  final val ServerPort = 8080

  val merchantService = new MerchantService
  val itemService = new ItemService

  def main(args: Array[String]): Unit = {

    val allRoutes = merchantService.route ~ itemService.route

    val server = Http().bindAndHandle(allRoutes, "localhost", ServerPort)
    println(s"http server running at http://localhost:$ServerPort/")
    println("Press return to exit")

    StdIn.readLine()

    system.terminate().onComplete(shutdown =>
      if (shutdown.isSuccess) println(s"Akka shutdown successful")
      else println(s"Problem shutting down Akka system ${shutdown.failed}")
    )
    server.flatMap(_.unbind()).onComplete(shutdown =>
      if (shutdown.isSuccess) println(s"HTTP server shutdown successful")
      else println(s"Problem shutting down HTTP server: ${shutdown.failed}")
    )
  }
}