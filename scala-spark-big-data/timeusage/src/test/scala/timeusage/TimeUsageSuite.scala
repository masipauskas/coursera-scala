package timeusage

import org.apache.spark.sql.SparkSession
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{BeforeAndAfterAll, FunSuite}

@RunWith(classOf[JUnitRunner])
class TimeUsageSuite extends FunSuite with BeforeAndAfterAll {
  val spark: SparkSession =
    SparkSession
      .builder()
      .appName("Time Usage")
      .config("spark.master", "local")
      .getOrCreate()

  test("timeUsageGrouped should return the same data as timeUsageGroupedSql") {
    import TimeUsage._
    val (columns, initDf) = read("/timeusage/atussum.csv")
    val (primaryNeedsColumns, workColumns, otherColumns) = classifiedColumns(columns)
    val summaryDf = timeUsageSummary(primaryNeedsColumns, workColumns, otherColumns, initDf)
    val finalGroupedDf = timeUsageGrouped(summaryDf)
    val finalGroupedSqlDf = timeUsageGroupedSql(summaryDf)

    val matches = finalGroupedDf.collect().zip(finalGroupedSqlDf.collect()).forall(pair => pair._1 == pair._2)
    if (!matches) {
      finalGroupedDf.show()
      finalGroupedSqlDf.show()
      assert(matches, s"Data frames should match")
    }
  }
}
