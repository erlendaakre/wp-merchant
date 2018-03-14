package com.frostvoid.wpMerchant.util

import scala.util.Random

trait IdGenerator {
  def generateId: Int = math.abs(Random.nextInt())
}
