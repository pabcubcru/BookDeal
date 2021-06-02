package infobooks

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class CreateListSentCancelAndDeleteRequest extends Simulation {

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
                                    "username" : "username1", 
                                    "password" : "password1" 
                                  } """)).asJson
            .headers(headers_3))
        .pause(5)
  }

  object GetBook {
		val getBook = exec(http("GetBook")
        .get("/books/book-001")
        .headers(headers_0))
      .pause(5)
	}

	object Create {
    val create = exec(http("CreateRequestForm")
            .get("/requests/book-001/new")
            .headers(headers_0))
        .pause(5)
        .exec(http("CreateRequestPost")
            .post("/requests/book-001/new")
            .body(StringBody(""" {
                                    "id" : "requestTest01",
                                    "username1" : null,
                                    "username2" : null,
                                    "idBook1" : null,
                                    "idBook2" : "book-001",
                                    "status" : null,
                                    "action" : "COMPRA",
                                    "pay" : 10.0,
                                    "comment" : "Comment"
                                  } """)).asJson
            .headers(headers_3))
        .pause(5)
  }

  object ListSentRequests {
		val listSentRequests = exec(http("ListSentRequests")
        .get("/requests/me/0")
        .headers(headers_0))
      .pause(5)
	}

  object Cancel {
		val cancel = exec(http("CancelRequest")
        .get("/requests/requestTest01/cancel")
        .headers(headers_0))
      .pause(5)
	}

  object Delete {
		val delete = exec(http("DeleteRequest")
        .delete("/requests/requestTest01/delete")
        .headers(headers_0))
      .pause(5)
	}

  val positiveScn = scenario("Create, List Sent, Cancel And Delete Request").exec(Home.home, Login.login, GetBook.getBook, Create.create, ListSentRequests.listSentRequests, Cancel.cancel, Delete.delete)

    setUp(
		positiveScn.inject(rampUsers(450) during (5 seconds))
	).protocols(httpProtocol)
	.assertions(
		global.responseTime.max.lt(5000),
		global.responseTime.mean.lt(1000),
		global.successfulRequests.percent.gt(95))
}