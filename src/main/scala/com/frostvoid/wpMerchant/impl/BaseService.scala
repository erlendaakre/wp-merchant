package com.frostvoid.wpMerchant.impl

import com.frostvoid.wpMerchant.util.{AkkaSupport, JsonSupport}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.{PathMatcher, Route}

trait BaseService extends JsonSupport {
  final val ApiRoot = "api"
  final val ApiVersion = "v1"

  def serviceName: String
  def servicePath: PathMatcher[Unit] = ApiRoot / ApiVersion / serviceName

  def route: Route
}
