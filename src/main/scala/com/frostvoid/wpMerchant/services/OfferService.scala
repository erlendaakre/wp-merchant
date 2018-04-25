package com.frostvoid.wpMerchant.services

import akka.actor.{ActorRef, ActorSystem, Props}
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.util.Timeout
import akka.pattern.ask
import com.frostvoid.wpMerchant.api._
import com.frostvoid.wpMerchant.impl.BaseService

/**
  * This actor defines the routes (API endpoints) for this service and handles incoming HTTP requests.
  * valid requests are handled by the OfferWorker and the messages returned are translated into the appropriate API response.
  *
  * @param system the actor system to use
  * @param timeout the akka timeout to use
  */
class OfferService(implicit val system: ActorSystem, implicit val timeout: Timeout) extends BaseService {
  final val serviceName = "offer"

  private val offerWorker: ActorRef = system.actorOf(Props[OfferWorker], "offerWorker")

  val route: Route =
    get {
      pathPrefix(servicePath) {
        path(IntNumber) { id =>
          onSuccess(offerWorker ? GetOfferRequest(id)) {
            case reply: OfferReturned => complete(StatusCodes.OK, reply.offer)
            case OfferNotFound => complete(StatusCodes.NotFound)
          }
        }
      }
    } ~
      post {
        path(servicePath) {
          entity(as[Offer]) { offer =>
            onSuccess(offerWorker ? AddOfferRequest(offer.merchant, offer.item, offer.price, offer.currency)) {
              case reply: OfferReturned => complete(StatusCodes.Created, reply.offer)
              case OfferNotCreatedBecauseMerchantInvalid => complete(StatusCodes.BadRequest, s"Invalid merchant id '${offer.merchant}'")
              case OfferNotCreatedBecauseItemInvalid => complete(StatusCodes.BadRequest, s"Invalid item id '${offer.item}'")
              case OfferNotCreatedBecauseNegativePrice => complete(StatusCodes.BadRequest, s"Price of item must be 0 or greater")
              case OfferNotCreated => complete(StatusCodes.InternalServerError, "unable to add new offer")
            }
          }
        }
      }}
