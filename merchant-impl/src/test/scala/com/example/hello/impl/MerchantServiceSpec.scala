package com.example.hello.impl

import java.util.UUID

import com.lightbend.lagom.scaladsl.server.LocalServiceLocator
import com.lightbend.lagom.scaladsl.testkit.ServiceTest
import org.scalatest.{AsyncWordSpec, BeforeAndAfterAll, FlatSpec, Matchers}
import com.frostvoid.wpMerchant.merchant.api.{CreateMerchantRequest, Merchant, MerchantService}
import com.frostvoid.wpMerchant.merchant.impl.{CreateMerchant, MerchantApplication}


class MerchantServiceSpec extends FlatSpec with Matchers with BeforeAndAfterAll {

//  private val server = ServiceTest.startServer(
//    ServiceTest.defaultSetup
//      .withCassandra()
//  ) { ctx =>
//    new MerchantApplication(ctx) with LocalServiceLocator
//  }

//  val client = server.serviceClient.implement[MerchantService]

//  override protected def afterAll() = server.stop()

  import scala.concurrent.ExecutionContext.Implicits.global
/*
  "The merchant service" should "not return a non-existing merchant " in
    ServiceTest.withServer(ServiceTest.defaultSetup) { ctx =>
      new MerchantApplication(ctx) with LocalServiceLocator
    } { server =>
      println("START")
      val client = server.serviceClient.implement[MerchantService]
      println("CALLING INVOKE")
      val merchant = client.getMerchant(UUID.randomUUID()).invoke()

      println("MERCHANT = " + merchant)
      merchant.map { response =>
        println("INVOKED GOT" + response)
        response should ===("Hello Alice!")
      }.onComplete(p => {
        println("SHIT")
        println(p)
      }
      )
    }*/


  "The merchant service" should "return a newly created merchant " in
    ServiceTest.withServer(ServiceTest.defaultSetup.withCassandra()) { ctx =>
      new MerchantApplication(ctx) with LocalServiceLocator
    } { server =>
      println("START")
      val client = server.serviceClient.implement[MerchantService]
      println("CREATING MERCHANT")
      val merchant = client.createMerchant.invoke(CreateMerchantRequest("Bob"))
      merchant.map(m =>
      println("CREATED MERCHANT " + m)
      ).onComplete(p => {
        println("MERCHANT CREATED SHIT")
          println(p)
      }
      )

      client.getMerchant(UUID.randomUUID()).invoke().map { response =>
        println("INVOKED GOT" + response)
        response should ===("Hello Alice!")
      }.onComplete(p => {
        println("SHIT")
        println(p)
      }
      )
    }



}
/*

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