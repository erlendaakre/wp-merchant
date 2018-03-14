package com.frostvoid.wpMerchant.impl

import akka.pattern.ask
import akka.actor.{ActorRef, ActorSystem, Props}
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.model.{ContentTypes, HttpEntity, StatusCodes}
import akka.http.scaladsl.server.Directives
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json._
import akka.stream.ActorMaterializer
import akka.util.Timeout
import com.frostvoid.wpMerchant.api._
import com.frostvoid.wpMerchant.services.MerchantActor
import com.frostvoid.wpMerchant.util.JsonSupport

import scala.io.StdIn
import scala.concurrent.duration._

/**
  * HTTP server responsible for redirecting different requests to the appropriate actors
  */
object HttpServer extends JsonSupport {

  final val ServerPort = 8080
  final val ApiRoot = "api"
  final val ApiVersion = "v1"

  private implicit val system = ActorSystem("wp-merchant")
  private implicit val materializer = ActorMaterializer()
  private implicit val executionContext = system.dispatcher
  private implicit val timeout = Timeout(3.seconds)

  val merchantActor: ActorRef = system.actorOf(Props[MerchantActor])

  def main(args: Array[String]): Unit = {

    val merchantRoutes =
      path(ApiRoot / ApiVersion / "merchant") {
        get {
          parameters('id.as[Int]) { id =>
            onSuccess(merchantActor ? GetMerchantRequest(id)) {
              case reply: MerchantReturned => complete(StatusCodes.OK, reply.merchant)
              case EmptyReply => complete(StatusCodes.NotFound)
            }
          }
        } ~
        post {
          entity(as[Merchant]) { merchant =>
            onSuccess(merchantActor ? AddMerchantRequest(merchant.name)) {
              case reply: MerchantReturned => complete(StatusCodes.Created, reply.merchant)
              case EmptyReply => complete(StatusCodes.InternalServerError, "unable to add new merchant")
            }
          }
        }
      }

    val itemRoutes =
      path(ApiRoot / ApiVersion / "item") {
        get {
          complete(StatusCodes.ServiceUnavailable, "Item API not yet implemented")
        }
      }



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