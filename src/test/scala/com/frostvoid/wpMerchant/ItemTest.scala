package com.frostvoid.wpMerchant

import akka.http.scaladsl.Http
import akka.http.scaladsl.marshalling.Marshal
import akka.http.scaladsl.model._
import akka.http.scaladsl.model.headers.RawHeader
import akka.http.scaladsl.unmarshalling.Unmarshal
import com.frostvoid.wpMerchant.api.Item
import com.frostvoid.wpMerchant.services.ItemService
import com.frostvoid.wpMerchant.util.{AkkaSupport, JsonSupport}
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpec}

import scala.concurrent.duration._
import scala.concurrent.{Await, Future}

class ItemTest extends WordSpec with Matchers with BeforeAndAfterAll with AkkaSupport with JsonSupport {

  private var server: Future[Http.ServerBinding] = _
  private val reqTimeout = 10.seconds

  override def beforeAll {
    val itemService = new ItemService
    server = Http().bindAndHandle(itemService.route, "localhost", 8080)
  }

  override def afterAll {
    system.terminate()
    server.flatMap(_.unbind())
  }

  "The Item Service" should {

    "not return an item if given a non-existent ID" in {
      val req = HttpRequest(uri = "http://localhost:8080/api/v1/item/123456")
      val res = Await.result(Http().singleRequest(req), reqTimeout)

      res.status shouldBe StatusCodes.NotFound
    }


    val newItem = Item(0, "Duotronic enhancer", "Enhances all things duotronic!")
    var createdItemId = 0 // returned by POST to /item
    "allow a new item to be created" in {
      val newItemEntity = Await.result(Marshal(newItem).to[MessageEntity], 1.second)

      val req = HttpRequest(uri = "http://localhost:8080/api/v1/item", method = HttpMethods.POST,
        headers = List(RawHeader("Content-Type", "application/json")), entity = newItemEntity)
      val res = Await.result(Http().singleRequest(req), reqTimeout)

      res.status shouldBe StatusCodes.Created
      val createdItem = Await.result(Unmarshal(res.entity).to[Item], 1.second)
      createdItem.name shouldBe newItem.name
      createdItem.description shouldBe newItem.description
      createdItem.id should be >= 1
      createdItemId = createdItem.id
    }


    "retrieve a newly created item by ID " in {
      val req = HttpRequest(uri = s"http://localhost:8080/api/v1/item/$createdItemId")
      val res = Await.result(Http().singleRequest(req), reqTimeout)

      res.status shouldBe StatusCodes.OK
      val retrievedItem = Await.result(Unmarshal(res.entity).to[Item], 1.second)
      retrievedItem.name shouldBe newItem.name
      retrievedItem.description shouldBe newItem.description
      retrievedItem.id shouldBe createdItemId
    }
  }
}
