package observatory.utils

import observatory.TestingContext
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{FunSuite, Matchers}

@RunWith(classOf[JUnitRunner])
class TemperatureTest extends FunSuite with Matchers with TestingContext {
  import Temperature._

  test("fahrenheitToCelsius should convert temperature to 0C correctly") {
    fahrenheitToCelsius(32) shouldEqual 0
  }

  test("fahrenheitToCelsius should convert temperature to -23.33C correctly") {
    fahrenheitToCelsius(-10) shouldEqual -23.33 +- tolerance
  }

  test("fahrenheitToCelsius should convert temperature to 32.22C correctly") {
    fahrenheitToCelsius(90) shouldEqual 32.22 +- tolerance
  }
}
