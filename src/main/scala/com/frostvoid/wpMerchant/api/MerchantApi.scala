package com.frostvoid.wpMerchant.api

// Models
case class Merchant(id: Int, name: String)
case class Item(id: Int, name: String, description: String)
case class Offer(id: Int, merchant: Int, price: Double, currency: String, start: String, end: String)

// Actor Requests and responses
sealed trait ApiRequest

// Merchant
sealed trait MerchantRequest
sealed trait MerchantReply

case class GetMerchantRequest(id: Int) extends MerchantRequest
case class AddMerchantRequest(name: String) extends MerchantRequest

case class MerchantReturned(merchant: Merchant) extends MerchantReply


// Item
sealed trait ItemRequest
sealed trait ItemReply

case class GetItemRequest(id: Int) extends ItemRequest
case class AddItemRequest(name: String, description: String) extends ItemRequest

case class ItemReturned(item: Item) extends ItemReply


// Offer
sealed trait OfferRequest
sealed trait OfferReply

case class GetOfferRequest(id: Int) extends OfferRequest


// Common
case object EmptyReply extends MerchantReply with ItemReply with OfferReply