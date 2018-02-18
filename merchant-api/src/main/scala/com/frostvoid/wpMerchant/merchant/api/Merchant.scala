package com.frostvoid.wpMerchant.merchant.api

import java.util.UUID

import play.api.libs.json.{Format, Json}

// Model
case class Merchant(id: Option[UUID], name: String) {
  def safeId: UUID = id.getOrElse(UUID.randomUUID())
}

object Merchant {
  implicit val format: Format[Merchant] = Json.format
}


// Commands
case class CreateMerchant(name: String)

object CreateMerchant {
  implicit val format: Format[CreateMerchant] = Json.format
}


// Events
sealed trait MerchantEvent {
  def id: UUID
}

case class MerchantCreated(merchant: Merchant) extends MerchantEvent {
  override def id = merchant.safeId
}

object MerchantCreated {
  implicit val format: Format[MerchantCreated] = Json.format
}