package observatory

import org.scalatest.prop.Checkers
import org.scalatest.{FunSuite, Matchers}

trait Visualization2Test extends FunSuite with Checkers with Matchers {
  import Visualization2._
  test("bilinearInterpolation should interpolate top-left corner correctly") {
    bilinearInterpolation(0, 0, -50, 34.48, -50, 0) shouldEqual -50
    bilinearInterpolation(0, 0, 10, 34.48, -50, 0) shouldEqual 10
  }

  test("bilinearInterpolation should interpolate values in the grid correctly") {
    bilinearInterpolation(0, 0.1, 26.953668506557932, 1.0, -1.0, -1.6753685755722216) shouldEqual 24.358301655902142
  }
}
