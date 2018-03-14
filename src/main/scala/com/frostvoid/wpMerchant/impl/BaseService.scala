package com.frostvoid.wpMerchant.impl

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.util.Timeout
import com.frostvoid.wpMerchant.util.JsonSupport

import scala.concurrent.ExecutionContextExecutor
import scala.concurrent.duration._

trait BaseService extends JsonSupport {
  final val ServerPort = 8080
  final val ApiRoot = "api"
  final val ApiVersion = "v1"

  implicit val system = ActorSystem("wp-merchant")
  implicit val materializer = ActorMaterializer()
  implicit val executionContext: ExecutionContextExecutor = system.dispatcher
  implicit val timeout = Timeout(3.seconds)
}
