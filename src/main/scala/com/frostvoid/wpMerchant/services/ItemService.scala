package com.frostvoid.wpMerchant.services

import akka.actor.{ActorRef, ActorSystem, Props}
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.pattern.ask
import akka.util.Timeout
import com.frostvoid.wpMerchant.api._
import com.frostvoid.wpMerchant.impl.BaseService

class ItemService(implicit val system: ActorSystem, implicit val timeout: Timeout) extends BaseService {
  final val serviceName = "item"

  private val itemWorker: ActorRef = system.actorOf(Props[ItemWorker], "itemWorker")

  val route: Route =
    get {
      pathPrefix(servicePath) {
        path(IntNumber) { id =>
          onSuccess(itemWorker ? GetItemRequest(id)) {
            case reply: ItemReturned => complete(StatusCodes.OK, reply.item)
            case EmptyReply => complete(StatusCodes.NotFound)
          }
        }
      }
    } ~
      post {
        path(servicePath) {
          entity(as[Item]) { item =>
            onSuccess(itemWorker ? AddItemRequest(item.name, item.description)) {
              case reply: ItemReturned => complete(StatusCodes.Created, reply.item)
              case EmptyReply => complete(StatusCodes.InternalServerError, "unable to add new item")
            }
          }
        }
      }
}
