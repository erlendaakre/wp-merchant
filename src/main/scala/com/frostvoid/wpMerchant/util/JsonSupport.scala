package com.frostvoid.wpMerchant.util

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import com.frostvoid.wpMerchant.api.{Item, Merchant, Offer}
import spray.json.DefaultJsonProtocol

trait JsonSupport extends SprayJsonSupport with DefaultJsonProtocol {
  implicit val merchantFormat = jsonFormat2(Merchant)
  implicit val itemFormat = jsonFormat3(Item)
  implicit val offerFormat = jsonFormat6(Offer)
}