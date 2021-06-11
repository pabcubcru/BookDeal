package bookdeal

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class AddListAndDeleteFavouriteBooks extends Simulation {

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
                                    "username" : "test001", 
                                    "password" : "test001" 
                                  } """)).asJson
            .headers(headers_3))
        .pause(5)
  }

  object Add {
		val add = exec(http("AddFavouriteBook")
        .get("/favourites/book-003/add")
        .headers(headers_0))
      .pause(5)
	}

  object List {
		val list = exec(http("ListFavouriteBooks")
        .get("/favourites/all")
        .headers(headers_0))
      .pause(5)
	}

  object Delete {
		val delete = exec(http("DeleteFavouriteBook")
        .delete("/favourites/ufb-004/delete")
        .headers(headers_0))
      .pause(5)
	}

  val positiveScn = scenario("Add, List And Delete Favourite Books").exec(Home.home, Login.login, Add.add, List.list, Delete.delete)

    setUp(
		positiveScn.inject(rampUsers(350) during (5 seconds))
	).protocols(httpProtocol)
	.assertions(
		global.responseTime.max.lt(5000),
		global.responseTime.mean.lt(1000),
		global.successfulRequests.percent.gt(95))
}