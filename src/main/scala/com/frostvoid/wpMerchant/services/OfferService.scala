package com.frostvoid.wpMerchant.services

import akka.actor.ActorSystem
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.util.Timeout
import com.frostvoid.wpMerchant.impl.BaseService

/**
  * This actor defines the routes (API endpoints) for this service and handles incoming HTTP requests.
  * valid requests are handled by the OfferWorker and the messages returned are translated into the appropriate API response.
  *
  * @param system the actor system to use
  * @param timeout the akka timeout to use
  */
class OfferService(implicit val system: ActorSystem, implicit val timeout: Timeout) extends BaseService {
  final val serviceName = "offer"

  /*
    TODO: add routes and implement OfferWorker for:
      POST  /offer
      GET   /offer/{offerId}
      GET   /offer/findByMerchant/{merchantId}
   */

  val route: Route =
    get {
      pathPrefix(servicePath) {
        path(IntNumber) { id =>
          complete(StatusCodes.ServiceUnavailable, "Offer API not yet implemented")
        }
      }
    }
}
