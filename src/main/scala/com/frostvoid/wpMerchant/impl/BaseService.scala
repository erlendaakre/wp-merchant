package com.frostvoid.wpMerchant.impl

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.{PathMatcher, Route}
import com.frostvoid.wpMerchant.util.JsonSupport

/**
  * Provides a common interface and functionality for all the services
  */
trait BaseService extends JsonSupport {
  final val ApiRoot = "api"
  val ApiVersion = "v1"

  def serviceName: String

  def servicePath: PathMatcher[Unit] = ApiRoot / ApiVersion / serviceName

  def route: Route
}
