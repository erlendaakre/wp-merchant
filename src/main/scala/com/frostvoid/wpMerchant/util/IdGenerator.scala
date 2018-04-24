package com.frostvoid.wpMerchant.util

import scala.util.Random

/**
  * Simple random ID generation functionality
  */
trait IdGenerator {
  def generateId: Int = math.abs(Random.nextInt())
}
