package com.frostvoid.wpMerchant.util

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import com.frostvoid.wpMerchant.api.{Item, Merchant, Offer}
import spray.json.{DefaultJsonProtocol, RootJsonFormat}

trait JsonSupport extends SprayJsonSupport with DefaultJsonProtocol {
  implicit val merchantFormat: RootJsonFormat[Merchant] = jsonFormat2(Merchant)
  implicit val itemFormat: RootJsonFormat[Item] = jsonFormat3(Item)
  implicit val offerFormat: RootJsonFormat[Offer] = jsonFormat6(Offer)
}