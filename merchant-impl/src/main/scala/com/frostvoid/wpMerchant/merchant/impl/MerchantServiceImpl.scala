package com.frostvoid.wpMerchant.merchant.impl

import java.util.UUID

import akka.actor.ActorSystem
import akka.stream.Materializer
import com.frostvoid.wpMerchant.merchant.api.MerchantService
import com.lightbend.lagom.scaladsl.api.ServiceCall
import com.lightbend.lagom.scaladsl.api.transport.NotFound
import com.lightbend.lagom.scaladsl.persistence.PersistentEntityRegistry

import scala.concurrent.ExecutionContext

/**
  * Implementation of the MerchantService.
  */
class MerchantServiceImpl(persistentEntityRegistry: PersistentEntityRegistry, system: ActorSystem)(implicit ec: ExecutionContext, mat: Materializer) extends MerchantService {

  private def refFor(userId: UUID) = persistentEntityRegistry.refFor[MerchantEntity](userId.toString)

  override def getMerchant(id: UUID) = ServiceCall { _ =>
    refFor(id).ask(GetMerchant).map {
      case Some(merchant) =>
        com.frostvoid.wpMerchant.merchant.api.Merchant(Some(id), merchant.name)
      case None =>
        throw NotFound(s"Merchant with id $id")
    }
  }

  override def createMerchant = ServiceCall { createMerchant =>
    val newId = UUID.randomUUID()

    refFor(newId).ask(CreateMerchant(createMerchant.name)).map { _ =>
      com.frostvoid.wpMerchant.merchant.api.Merchant(Some(newId), createMerchant.name)
    }
  }
}
