package bookdeal

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class ListMeGetEditAndDeleteBook extends Simulation {

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

  object ListMyBooks {
		val listMyBooks = exec(http("ListMyBooks")
        .get("/books/me/0")
        .headers(headers_0))
      .pause(5)
	}

  object GetBook {
		val getBook = exec(http("GetBook")
        .get("/books/bookTest01")
        .headers(headers_0))
      .pause(5)
	}

	object Edit {
    val edit = exec(http("EditBookForm")
            .get("/books/book-001/edit")
            .headers(headers_0))
        .pause(5)
        .exec(http("EditBookPost")
            .post("/books/book-001/edit")
            .body(StringBody(""" {
                                    "id" : "book-001",
                                    "title" : "Titletest",
                                    "originalTitle" : null,
                                    "isbn" : "0-7645-2641-3",
                                    "publicationYear" : 2010,
                                    "publisher" : "PublisherTest",
                                    "genres" : "Gastronomía,Cocina",
                                    "author" : "Author Test",
                                    "description" : "Description Test",
                                    "status" : "COMO NUEVO",
                                    "price" : 10.0,
                                    "image" : "0"
                                  } """)).asJson
            .headers(headers_3))
        .pause(5)
  }

  object Delete {
		val delete = exec(http("DeleteBook")
        .delete("/books/book-001/delete")
        .headers(headers_0))
      .pause(5)
	}

    val positiveScn = scenario("List Me, Get, Edit And Delete Book").exec(Home.home, Login.login, ListMyBooks.listMyBooks, GetBook.getBook, Edit.edit, GetBook.getBook, Delete.delete)

    setUp(
		positiveScn.inject(rampUsers(400) during (5 seconds))
	).protocols(httpProtocol)
	.assertions(
		global.responseTime.max.lt(5000),
		global.responseTime.mean.lt(1000),
		global.successfulRequests.percent.gt(95))
}