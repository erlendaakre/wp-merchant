package com.frostvoid.wpMerchant.util

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.util.Timeout
import scala.concurrent.duration._

import scala.concurrent.ExecutionContextExecutor

trait AkkaSupport {
  implicit val system = ActorSystem("wp-merchant")
  implicit val materializer = ActorMaterializer()
  implicit val executionContext: ExecutionContextExecutor = system.dispatcher
  implicit val timeout = Timeout(3.seconds)
}
