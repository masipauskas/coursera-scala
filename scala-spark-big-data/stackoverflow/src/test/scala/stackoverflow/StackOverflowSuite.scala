package stackoverflow

import org.scalatest.{FunSuite, BeforeAndAfterAll}
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
import org.apache.spark.rdd.RDD
import java.io.File

@RunWith(classOf[JUnitRunner])
class StackOverflowSuite extends FunSuite with BeforeAndAfterAll with SparkHelpers with PostingHelpers {

  lazy val testObject = new StackOverflow {
    override val langs =
      List(
        "JavaScript", "Java", "PHP", "Python", "C#", "C++", "Ruby", "CSS",
        "Objective-C", "Perl", "Scala", "Haskell", "MATLAB", "Clojure", "Groovy")
    override def langSpread = 50000
    override def kmeansKernels = 45
    override def kmeansEta: Double = 20.0D
    override def kmeansMaxIterations = 120
  }

  test("testObject can be instantiated") {
    val instantiatable = try {
      testObject
      true
    } catch {
      case ex: Throwable =>
        ex.printStackTrace()
        false
    }
    assert(instantiatable, "Can't instantiate a StackOverflow object")
  }

  test("groupedPostings should join answers and questions correctly") {
    val scalaQuestion = question(1, "Scala", 10)
    val scalaFirstAnswer = answer(1, 5)
    val scalaSecondAnswer = answer(1, 3)
    val javaQuestion = question(2, "Java", 15)
    val postings = rdd(scalaQuestion, scalaFirstAnswer, scalaSecondAnswer, javaQuestion)
    val expected = Array((1, Seq((scalaQuestion, scalaFirstAnswer), (scalaQuestion, scalaSecondAnswer))))

    val result = testObject.groupedPostings(postings)

    result.foreach(pair => println(s"id: ${pair._1}, values: ${pair._2.toSeq}"))

    result.collect().zip(expected)
      .foreach { case (res, act) =>
        assert(res._1 == act._1)
        assert(res._2.toSeq == act._2)
      }
  }

  test("clusterResults should report data correctly") {
    val points = rdd((0, 10), (50000, 20), (150000, 10))
    val means = Array((50000, 10), (100000, 20))

    val result = testObject.clusterResults(means, points)

    val expected = Seq(
      ("Python", 100d, 1, 10),
      ("Java", 50d, 2, 15)
    )

    equals(result, expected)
  }
}
