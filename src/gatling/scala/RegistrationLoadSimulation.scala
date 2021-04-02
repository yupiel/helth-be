import io.gatling.core.Predef.{exec, _}
import io.gatling.core.feeder.Feeder
import io.gatling.http.Predef._

import scala.concurrent.duration.DurationInt
import scala.language.postfixOps
import scala.util.Random

class RegistrationLoadSimulation extends Simulation {

  val userRegistrationScenario =
    scenario("User Registration")
    .exec(
    http("Users")
      .post("/")
      .body(
        StringBody( session =>
          s"""{ "username" :  "${Random.alphanumeric.take(8).mkString}","""
          + s""" "password" : "${Random.alphanumeric.take(8).mkString}" }"""
        )
      ).asJson
      .check(status.is(200))
      .check(jsonPath("$.id").saveAs("userRegistrationIdResponse"))
    )

  val httpProtocol = http
    .baseUrl("https://helthpiel.herokuapp.com/users")

  setUp(userRegistrationScenario.inject(
    /*atOnceUsers(64)*/rampUsersPerSec(2).to(64).during(60 seconds)
  ).protocols(httpProtocol))
}