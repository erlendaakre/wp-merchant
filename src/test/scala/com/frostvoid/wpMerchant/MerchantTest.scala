package com.frostvoid.wpMerchant

import org.scalatest.{Matchers, WordSpec}
import akka.http.scaladsl.model.{HttpRequest, StatusCodes}
import akka.http.scaladsl.testkit.ScalatestRouteTest
import akka.http.scaladsl.server._
import org.scalatest.{Matchers, WordSpec}
import Directives._
import com.frostvoid.wpMerchant.services.MerchantService

class MerchantTest extends  WordSpec with Matchers with ScalatestRouteTest {
  "The Merchant Service" should {

    "not return a merchant if given a non-existent ID" in {
//      val req = HttpRequest(uri = "http://localhost:8080/api/v1/merchant?id=500")
//      Get() ~>  check {
//        responseAs[String] shouldEqual "fff"
//      }



    }
  }
}
