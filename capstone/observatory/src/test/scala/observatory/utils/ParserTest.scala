package observatory.utils

import java.time.LocalDate

import akka.japi.Option.Some
import observatory.Location
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{FunSuite, Matchers}

@RunWith(classOf[JUnitRunner])
class ParserTest extends FunSuite with Matchers {
  import Parser._

  test("isValid should be valid for Some(_)") {
    isValid(Some(0)) shouldEqual true
  }

  test("isValid should be not valid for None") {
    isValid(None) shouldEqual false
  }

  test("parseStation should return some(station) for valid station") {
    parseStation("724017,03707,+37.358,-078.438") shouldEqual Option(("72401703707", Location(37.358, -078.438)))
  }

  test("parseStation should return some(station) for valid station with only one key component present") {
    parseStation("724017,,+37.358,-078.438") shouldEqual Option(("724017", Location(37.358, -078.438)))
  }

  test("parseStation should return none for invalid station") {
    parseStation("010013,,,") shouldEqual None
  }

  test("parseObservation should return some(observation) for valid station") {
    parseObservation("724017,03707,12,06,32", 2015) shouldEqual Option(("72401703707", LocalDate.of(2015, 12, 6), 0))
  }

  test("parseObservation should return None for invalid observation where temperature is 9999") {
    parseObservation("724017,03707,12,06,9999", 2015) shouldEqual None
  }
}