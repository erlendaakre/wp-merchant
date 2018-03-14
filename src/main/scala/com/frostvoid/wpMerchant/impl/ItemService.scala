package com.frostvoid.wpMerchant.impl

import akka.http.scaladsl.server.Directives.{complete, get, path}
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route

trait ItemService extends BaseService {
  val itemRoutes: Route =
    path(ApiRoot / ApiVersion / "item") {
      get {
        complete(StatusCodes.ServiceUnavailable, "Item API not yet implemented")
      }
    }
}
