package com.frostvoid.wpMerchant.merchant.api

import java.util.UUID

import akka.NotUsed
import com.lightbend.lagom.scaladsl.api.broker.Topic
import com.lightbend.lagom.scaladsl.api.broker.kafka.{KafkaProperties, PartitionKeyStrategy}
import com.lightbend.lagom.scaladsl.api.{Service, ServiceCall}

object MerchantService {
  val TopicName = "merchant"
  val RootPath = "/api/v1/merchant"
}

/**
  * The merchant service interface definition
  */
trait MerchantService extends Service {

  def getMerchant(id: UUID): ServiceCall[NotUsed, Merchant]

  def createMerchant: ServiceCall[CreateMerchant, Merchant]

  override final def descriptor = {
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
