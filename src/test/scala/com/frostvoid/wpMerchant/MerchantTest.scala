package com.frostvoid.wpMerchant

import akka.http.scaladsl.Http
import akka.http.scaladsl.marshalling.Marshal
import akka.http.scaladsl.model._
import akka.http.scaladsl.model.headers.RawHeader
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.unmarshalling.Unmarshal
import com.frostvoid.wpMerchant.api.Merchant
import com.frostvoid.wpMerchant.services.{ItemService, MerchantService}
import com.frostvoid.wpMerchant.util.{AkkaSupport, JsonSupport}
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpec}

import scala.concurrent.duration._
import scala.concurrent.{Await, Future}

class MerchantTest extends WordSpec with Matchers with BeforeAndAfterAll with AkkaSupport with JsonSupport {

  private var server: Future[Http.ServerBinding] = _
  private val reqTimeout = 10.seconds

  override def beforeAll {
    val merchantService = new MerchantService
    val itemService = new ItemService
    val allRoutes = merchantService.route ~ itemService.route
    server = Http().bindAndHandle(allRoutes, "localhost", 8080)
  }

  override def afterAll {
    system.terminate()
    server.flatMap(_.unbind())
  }

  "The Merchant Service" should {
    "not return a merchant if given a non-existent ID" in {
      val req = HttpRequest(uri = "http://localhost:8080/api/v1/merchant")
      val res = Await.result(Http().singleRequest(req), reqTimeout)

      res.status shouldBe StatusCodes.NotFound
    }
  }

  "The Merchant Service" should {
    val newMerchant = Merchant(0, "Test Merchant")
    var createdMerchantId = 0 // returned by POST to /merchant

    "allow a new merchant to be created" in {
      val newMerchantEntity = Await.result(Marshal(newMerchant).to[MessageEntity], 1.second)

      val req = HttpRequest(uri = "http://localhost:8080/api/v1/merchant?id=500", method = HttpMethods.POST,
        headers = List(RawHeader("Content-Type", "application/json")), entity = newMerchantEntity)
      val res = Await.result(Http().singleRequest(req), reqTimeout)

      res.status shouldBe StatusCodes.Created
      val createdMerchant = Await.result(Unmarshal(res.entity).to[Merchant], 1.second)
      createdMerchant.name shouldBe newMerchant.name
      createdMerchant.id should be >= 1
      createdMerchantId = createdMerchant.id
    }
    "retrieve a newly created merchant by ID " in {
      val req = HttpRequest(uri = s"http://localhost:8080/api/v1/merchant/$createdMerchantId")
      val res = Await.result(Http().singleRequest(req), reqTimeout)

      res.status shouldBe StatusCodes.OK
      val retrievedMerchant = Await.result(Unmarshal(res.entity).to[Merchant], 1.second)
      retrievedMerchant.name shouldBe newMerchant.name
      retrievedMerchant.id shouldBe createdMerchantId
    }
  }

}
