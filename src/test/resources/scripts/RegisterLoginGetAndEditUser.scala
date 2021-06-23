package bookdeal

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class RegisterLoginGetAndEditUser extends Simulation {

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

  object Register {
    val register = exec(http("RegisterUserForm")
            .get("/register")
            .headers(headers_0))
        .pause(5)
        .exec(http("RegisterUserPost")
            .post("/register")
            .body(StringBody(""" {
                                    "id" : "userTest01",
                                    "name" : "Test",
                                    "email" : "email@email.com",
                                    "phone" : "+34654987321",   
                                    "birthDate" : "1998-01-01", 
                                    "province" : "Sevilla",    
                                    "postCode" : "41012",
                                    "genres" : "Religión,Gastronomía,Cocina",
                                    "username" : "userTest",
                                    "password" : "password123",
                                    "enabled" : true,
                                    "accept" : true,
                                    "confirmPassword" : "password123"
                                  } """)).asJson
            .headers(headers_3))
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
                                    "username" : "userTest", 
                                    "password" : "password123" 
                                  } """)).asJson
            .headers(headers_3))
        .pause(5)
  }

  object Get {
		val get = exec(http("GetUser")
			.get("/profile")
			.headers(headers_0))
		  .pause(5)
	}

  object Edit {
    val edit = exec(http("EditUserPost")
            .put("/user/userTest01/edit")
            .body(StringBody(""" {
                                "id" : "userTest01",
                                "name" : "TestEdit",
                                "email" : "emailedit@email.com",
                                "phone" : "+34654987321",   
                                "birthDate" : "1985-01-01", 
                                "province" : "Province",    
                                "postCode" : "41012",
                                "genres" : "Religión,Gastronomía,Cocina",
                                "username" : "userTest",
                                "password" : "password123",
                                "enabled" : true,
                                "accept" : true,
                                "confirmPassword" : "password123"
                              } """)).asJson
            .headers(headers_3))
        .pause(5)
  }

    val positiveScn = scenario("Register, Login, Get and Edit User").exec(Home.home, Register.register, Login.login, Get.get, Edit.edit)

    setUp(
		positiveScn.inject(rampUsers(375) during (5 seconds))
	).protocols(httpProtocol)
	.assertions(
		global.responseTime.max.lt(5000),
		global.responseTime.mean.lt(1000),
		global.successfulRequests.percent.gt(95))
}