package com.frostvoid.wpMerchant.merchant.api

import java.util.UUID

import akka.NotUsed
import com.lightbend.lagom.scaladsl.api.{Descriptor, Service, ServiceCall}

object MerchantService {
  val TopicName = "merchant"
  val RootPath = "/api/v1/merchant"
}

/**
  * The merchant service interface definition
  */
trait MerchantService extends Service {

  def getMerchant(id: UUID): ServiceCall[NotUsed, Merchant]

  def createMerchant: ServiceCall[CreateMerchantRequest, Merchant]

  override final def descriptor: Descriptor = {
    import MerchantService._
    import Service._

    named("merchant")
      .withCalls(
        pathCall(s"$RootPath", createMerchant),
        pathCall(s"$RootPath/:id", getMerchant _)
      )
      .withAutoAcl(true)
  }
}
