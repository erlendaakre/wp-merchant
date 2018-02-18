package com.frostvoid.wpMerchant.merchant.impl

import akka.Done
import com.frostvoid.wpMerchant.merchant.util.JsonFormats._
import com.lightbend.lagom.scaladsl.persistence.PersistentEntity
import com.lightbend.lagom.scaladsl.persistence.PersistentEntity.ReplyType
import com.lightbend.lagom.scaladsl.playjson.{JsonSerializer, JsonSerializerRegistry}
import play.api.libs.json.{Format, Json}

/**
  * This is an event sourced entity representing a Merchant
  *
  * It can receive a [[CreateMerchant]] or [[GetMerchant]] command
  *
  * The [[MerchantCreated]] event is emitted when the [[CreateMerchant]] command is handled
  */
class MerchantEntity extends PersistentEntity {

  override type Command = MerchantCommand
  override type Event = MerchantEvent
  override type State = Option[Merchant]

  override def initialState: Option[Merchant] = None

  /**
    * Handles the commands based on current state and updates/retrieves state
    */
  override def behavior: Behavior = {
    case Some(merchant) =>
      Actions().onReadOnlyCommand[GetMerchant.type, Option[Merchant]] {
        case (GetMerchant, ctx, state) => ctx.reply(state)
      }.onReadOnlyCommand[CreateMerchant, Done] {
        case (CreateMerchant(name), ctx, state) => ctx.invalidCommand("Merchant already exists")
      }
    case None =>
      Actions().onReadOnlyCommand[GetMerchant.type, Option[Merchant]] {
        case (GetMerchant, ctx, state) => ctx.reply(state)
      }.onCommand[CreateMerchant, Done] {
        case (CreateMerchant(name), ctx, state) => ctx.thenPersist(MerchantCreated(name))(_ => ctx.reply(Done))
      }.onEvent {
        case (MerchantCreated(name), state) => Some(Merchant(name))
      }
  }
}

case class Merchant(name: String)
object Merchant {

  implicit val format: Format[Merchant] = Json.format
}

sealed trait MerchantCommand

case class CreateMerchant(name: String) extends MerchantCommand with ReplyType[Done]

object CreateMerchant {
  implicit val format: Format[CreateMerchant] = Json.format
}

case object GetMerchant extends MerchantCommand with ReplyType[Option[Merchant]] {
  implicit val format: Format[GetMerchant.type] = singletonFormat(GetMerchant)
}

sealed trait MerchantEvent

case class MerchantCreated(name: String) extends MerchantEvent

object MerchantCreated {
  implicit val format: Format[MerchantCreated] = Json.format
}


object MerchantSerializerRegistry extends JsonSerializerRegistry {
  override def serializers = List(
    JsonSerializer[Merchant],
    JsonSerializer[MerchantCreated],
    JsonSerializer[CreateMerchant],
    JsonSerializer[GetMerchant.type]
  )
}