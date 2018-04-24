package com.frostvoid.wpMerchant.services

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import com.frostvoid.wpMerchant.impl.BaseService

/**
  * @author eaakre - 2018-04-24
  */
class OfferService extends BaseService {
  final val serviceName = "offer"

  /*
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
