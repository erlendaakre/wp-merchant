package com.example.hello.impl

import java.util.UUID

import com.lightbend.lagom.scaladsl.server.LocalServiceLocator
import com.lightbend.lagom.scaladsl.testkit.ServiceTest
import org.scalatest.{AsyncWordSpec, BeforeAndAfterAll, FlatSpec, Matchers}
import com.frostvoid.wpMerchant.merchant.api.MerchantService
import com.frostvoid.wpMerchant.merchant.impl.MerchantApplication
import com.lightbend.lagom.scaladsl.api.transport.NotFound
import org.slf4j.{ Logger, LoggerFactory }


class MerchantServiceSpec extends FlatSpec with Matchers with BeforeAndAfterAll {

  private val server = ServiceTest.startServer(
    ServiceTest.defaultSetup
      .withCassandra()
  ) { ctx =>
    new MerchantApplication(ctx) with LocalServiceLocator
  }

  val client = server.serviceClient.implement[MerchantService]

  override protected def afterAll() = server.stop()


  "The merchant service" should "not return a non-existing merchant " in {
    import scala.concurrent.ExecutionContext.Implicits.global
   // assertThrows[NotFound] {
      client.getMerchant(UUID.randomUUID()).invoke().map { result =>
        log.error("FOO")
        log.error("got ", result)
        assert(result.name == "hey")
      }
   // }
  }

  private final val log: Logger = LoggerFactory.getLogger(classOf[MerchantServiceSpec])
}

    /*
        client.getMerchant(UUID.randomUUID()).invoke().map { result =>
      assert(result == NotFound)
    }
     */
/*
    "allow responding with a custom message" in {
      for {
        _ <- client.useGreeting("Bob").invoke(GreetingMessage("Hi"))
        answer <- client.hello("Bob").invoke()
      } yield {
        answer should ===("Hi, Bob!")
      }
    }
  }

}
*/