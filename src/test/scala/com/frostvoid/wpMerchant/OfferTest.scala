package com.frostvoid.wpMerchant

import akka.http.scaladsl.Http
import akka.http.scaladsl.marshalling.Marshal
import akka.http.scaladsl.model._
import akka.http.scaladsl.model.headers.RawHeader
import akka.http.scaladsl.unmarshalling.Unmarshal
import com.frostvoid.wpMerchant.api.{Item, Merchant, Offer}
import com.frostvoid.wpMerchant.services.OfferService
import com.frostvoid.wpMerchant.util.{AkkaSupport, JsonSupport}
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpec}

import scala.concurrent.duration._
import scala.concurrent.{Await, Future}

class OfferTest extends WordSpec with Matchers with BeforeAndAfterAll with AkkaSupport with JsonSupport {

  private var server: Future[Http.ServerBinding] = _
  private val reqTimeout = 10.seconds

  override def beforeAll {
    val offerService = new OfferService
    server = Http().bindAndHandle(offerService.route, "localhost", 8080)
  }

  override def afterAll {
    system.terminate()
    server.flatMap(_.unbind())
  }

  "The Offer Service" should {
    "not return an offer if given a non-existent ID" in {
      val req = HttpRequest(uri = "http://localhost:8080/api/v1/offer/1000000")
      val res = Await.result(Http().singleRequest(req), reqTimeout)

      res.status shouldBe StatusCodes.NotFound
    }


    var createdMerchantId = 0 // returned by POST to /merchant
    "not allow creation of an offer for a non-existent item" in {
      // create the merchant that the offer is being made by
      val createMerchantReq = HttpRequest(uri = "http://localhost:8080/api/v1/merchant", method = HttpMethods.POST,
        headers = List(RawHeader("Content-Type", "application/json")),
        entity = Await.result(Marshal(Merchant(0, "Vandelay Industries")).to[MessageEntity], 1.second))
      val createMerchantRes = Await.result(Http().singleRequest(createMerchantReq), reqTimeout)
      createMerchantRes.status shouldBe StatusCodes.Created
      createdMerchantId = Await.result(Unmarshal(createMerchantRes.entity).to[Merchant], 1.second).id
      createdMerchantId should be >= 1

      // attempt to create offer with valid merchant but invalid item id
      val newOffer = Offer(0, createdMerchantId, 5555555, 29.99, "GBP")
      val newOfferEntity = Await.result(Marshal(newOffer).to[MessageEntity], 1.second)
      val req = HttpRequest(uri = "http://localhost:8080/api/v1/offer", method = HttpMethods.POST,
        headers = List(RawHeader("Content-Type", "application/json")), entity = newOfferEntity)
      val res = Await.result(Http().singleRequest(req), reqTimeout)

      res.status shouldBe StatusCodes.BadRequest
    }


    var createdItemId = 0 // returned by POST to /item
    "not allow creation of an offer for a non-existent merchant" in {
      // create the item the offer is for
      val createItemReq = HttpRequest(uri = "http://localhost:8080/api/v1/item", method = HttpMethods.POST,
        headers = List(RawHeader("Content-Type", "application/json")),
        entity = Await.result(Marshal(Item(0, "Plumbus", "Made with high quality schleem")).to[MessageEntity], 1.second))
      val createItemRes = Await.result(Http().singleRequest(createItemReq), reqTimeout)
      createItemRes.status shouldBe StatusCodes.Created
      createdItemId = Await.result(Unmarshal(createItemRes.entity).to[Item], 1.second).id
      createdItemId should be >= 1

      // attempt to create offer for the valid item but invalid merchant id
      val newOffer = Offer(0, 42, createdItemId, 29.99, "GBP")
      val newOfferEntity = Await.result(Marshal(newOffer).to[MessageEntity], 1.second)
      val req = HttpRequest(uri = "http://localhost:8080/api/v1/offer", method = HttpMethods.POST,
        headers = List(RawHeader("Content-Type", "application/json")), entity = newOfferEntity)
      val res = Await.result(Http().singleRequest(req), reqTimeout)

      res.status shouldBe StatusCodes.BadRequest
    }


    var createdOfferId = 0 // returned by POST to /offer
    "allow a new offer to be created" in {
      val validOffer = Offer(0, createdMerchantId, createdItemId, 29.99, "GBP")
      val newOfferEntity = Await.result(Marshal(validOffer).to[MessageEntity], 1.second)
      val req = HttpRequest(uri = "http://localhost:8080/api/v1/offer", method = HttpMethods.POST,
        headers = List(RawHeader("Content-Type", "application/json")), entity = newOfferEntity)
      val res = Await.result(Http().singleRequest(req), reqTimeout)
      res.status shouldBe StatusCodes.Created

      createdOfferId = Await.result(Unmarshal(res.entity).to[Offer], 1.second).id
    }


    "retrieve a newly created offer by ID " in {
      val req = HttpRequest(uri = s"http://localhost:8080/api/v1/offer/$createdOfferId")
      val res = Await.result(Http().singleRequest(req), reqTimeout)

      res.status shouldBe StatusCodes.OK
      val retrievedOffer = Await.result(Unmarshal(res.entity).to[Offer], 1.second)
      retrievedOffer.item shouldBe createdItemId
      retrievedOffer.merchant shouldBe createdMerchantId
      retrievedOffer.id shouldBe createdOfferId
      retrievedOffer.price shouldBe 29.99
      retrievedOffer.currency shouldBe "GBP"
    }
  }

}
