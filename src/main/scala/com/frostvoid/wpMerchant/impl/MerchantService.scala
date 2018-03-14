package com.frostvoid.wpMerchant.impl

import akka.actor.{ActorRef, Props}
import akka.http.scaladsl.model.StatusCodes
import com.frostvoid.wpMerchant.api._
import com.frostvoid.wpMerchant.services.MerchantActor
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.pattern.ask

trait MerchantService extends BaseService {
  val merchantActor: ActorRef = system.actorOf(Props[MerchantActor])

  val merchantRoutes: Route =

    path(ApiRoot / ApiVersion / "merchant") {
      get {
        // TODO get ID from path instead of parameter
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
}
