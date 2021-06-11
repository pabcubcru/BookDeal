package bookdeal

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class ListReceivedAcceptAndRejectRequest extends Simulation {

  val httpProtocol = http
    .baseUrl("http://localhost:8080")
    .inferHtmlResources(BlackList(""".*.css""", """.*.js""", """.*.ico""", """.*.png"""), WhiteList())
    .userAgentHeader("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.138 Safari/537.36")

  val headers_0 = Map(
    "Accept" -> "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9",
    "Accept-Encoding" -> "gzip, deflate",
    "Accept-Language" -> "es-ES,es;q=0.9,en;q=0.8",
    "Proxy-Connection" -> "keep-alive",
    "Upgrade-Insecure-Requests" -> "1")

  val headers_3 = Map(
    "Accept" -> "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9",
    "Accept-Encoding" -> "gzip, deflate",
    "Accept-Language" -> "es-ES,es;q=0.9,en;q=0.8",
    "Origin" -> "http://localhost:8080",
    "Proxy-Connection" -> "keep-alive",
    "Upgrade-Insecure-Requests" -> "1")

  object Home {
		val home = exec(http("Home")
			.get("/")
			.headers(headers_0))
		.pause(5)
	}

  object Login {
    val login = exec(http("LoginForm")
            .get("/login")
            .headers(headers_0))
        .pause(5)
        .exec(http("LoginPost")
            .post("/login")
            .body(StringBody(""" { 
                                    "username" : "test002", 
                                    "password" : "test002" 
                                  } """)).asJson
            .headers(headers_3))
        .pause(5)
  }

  object ListReceivedRequests {
		val listReceivedRequests = exec(http("ListReceivedRequests")
        .get("/requests/received/0")
        .headers(headers_0))
      .pause(5)
	}

  object Accept {
		val accept = exec(http("AcceptRequest")
        .get("/requests/request-001/accept")
        .headers(headers_0))
      .pause(5)
	}

  object Reject {
		val reject = exec(http("RejectRequest")
        .delete("/requests/request-002/reject")
        .headers(headers_0))
      .pause(5)
	}

  val positiveScn = scenario("List Received, Accept And Reject Requests").exec(Home.home, Login.login, ListReceivedRequests.listReceivedRequests, Accept.accept, Reject.reject)

    setUp(
		positiveScn.inject(rampUsers(450) during (5 seconds))
	).protocols(httpProtocol)
	.assertions(
		global.responseTime.max.lt(5000),
		global.responseTime.mean.lt(1000),
		global.successfulRequests.percent.gt(95))
}