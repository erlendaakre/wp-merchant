package com.frostvoid.wpMerchant.services

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives.{complete, get, path, _}
import akka.http.scaladsl.server.Route
import com.frostvoid.wpMerchant.impl.BaseService

class ItemService extends BaseService {
  final val serviceName = "item"

  val route: Route =
    get {
      pathPrefix(servicePath) {
        path(IntNumber) { id =>
          complete(StatusCodes.ServiceUnavailable, "Item API not yet implemented")
        }
      }
    }
}
