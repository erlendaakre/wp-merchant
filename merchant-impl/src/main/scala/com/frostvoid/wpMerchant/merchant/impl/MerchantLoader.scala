package com.frostvoid.wpMerchant.merchant.impl

import com.frostvoid.wpMerchant.merchant.api.MerchantService
import com.lightbend.lagom.scaladsl.api.ServiceLocator
import com.lightbend.lagom.scaladsl.api.ServiceLocator.NoServiceLocator
import com.lightbend.lagom.scaladsl.broker.kafka.LagomKafkaComponents
import com.lightbend.lagom.scaladsl.devmode.LagomDevModeComponents
import com.lightbend.lagom.scaladsl.persistence.cassandra.CassandraPersistenceComponents
import com.lightbend.lagom.scaladsl.server._
import com.softwaremill.macwire._
import play.api.libs.ws.ahc.AhcWSComponents

class MerchantLoader extends LagomApplicationLoader {

  override def load(context: LagomApplicationContext): LagomApplication =
    new MerchantApplication(context) {
      override def serviceLocator: ServiceLocator = NoServiceLocator
    }

  override def loadDevMode(context: LagomApplicationContext): LagomApplication =
    new MerchantApplication(context) with LagomDevModeComponents

  override def describeService = Some(readDescriptor[MerchantService])
}

abstract class MerchantApplication(context: LagomApplicationContext)
  extends LagomApplication(context)
    with CassandraPersistenceComponents
    with LagomKafkaComponents
    with AhcWSComponents {

  // Bind the service that this server provides
  override lazy val lagomServer = serverFor[MerchantService](wire[MerchantServiceImpl])

  // Register the JSON serializer registry
  override lazy val jsonSerializerRegistry = MerchantSerializerRegistry

  // Register the merchant persistent entity
  persistentEntityRegistry.register(wire[MerchantEntity])
}
