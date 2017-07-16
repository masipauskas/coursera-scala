package observatory

import java.time.LocalDate

import org.junit.runner.RunWith
import org.scalatest.{FunSuite, Matchers}
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class ExtractionTest extends FunSuite with TestingContext with Matchers {
  import Extraction._

  test("locateTemperaturesRDD should extract temperatures correctly from temperatures and stations RDDs") {
    val stations = Seq(
      "010013,,,",
      "724017,03707,+37.358,-078.438",
      "724017,,+37.350,-078.433"
    ).rdd

    val temperatures = Seq(
      "010013,,11,25,39.2",
      "724017,,08,11,81.14",
      "724017,03707,12,06,32",
      "724017,03707,01,29,35.6"
    ).rdd

    val actual = locateTemperaturesRDD(2015, stations, temperatures).collect()

    actual shouldEqual Seq(
      (LocalDate.of(2015, 8, 11), Location(37.35, -78.433), 27.3),
      (LocalDate.of(2015, 12, 6), Location(37.358, -78.438), 0.0),
      (LocalDate.of(2015, 1, 29), Location(37.358, -78.438), 2.0)
    )
  }
  
}