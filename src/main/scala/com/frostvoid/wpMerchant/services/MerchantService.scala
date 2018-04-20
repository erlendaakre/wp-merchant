package com.frostvoid.wpMerchant.services

import akka.actor.{ActorRef, ActorSystem, Props}
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.pattern.ask
import akka.util.Timeout
import com.frostvoid.wpMerchant.api._
import com.frostvoid.wpMerchant.impl.BaseService

class MerchantService(implicit val system: ActorSystem, implicit val timeout: Timeout) extends BaseService {
  final val serviceName = "merchant"

  private val merchantWorker: ActorRef = system.actorOf(Props[MerchantWorker], "merchantWorker")

  val route: Route =
    get {
      pathPrefix(servicePath) {
        path(IntNumber) { id =>
          onSuccess(merchantWorker ? GetMerchantRequest(id)) {
            case reply: MerchantReturned => complete(StatusCodes.OK, reply.merchant)
            case EmptyReply => complete(StatusCodes.NotFound)
          }
        }
      }
    } ~
      post {
        path(servicePath) {
          entity(as[Merchant]) { merchant =>
            onSuccess(merchantWorker ? AddMerchantRequest(merchant.name)) {
              case reply: MerchantReturned => complete(StatusCodes.Created, reply.merchant)
              case EmptyReply => complete(StatusCodes.InternalServerError, "unable to add new merchant")
            }
          }
        }
      }
}
