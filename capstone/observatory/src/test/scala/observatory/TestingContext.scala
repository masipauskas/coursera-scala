package observatory

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

import scala.reflect.ClassTag

trait TestingContext {
  import org.apache.log4j.{Level, Logger}
  Logger.getLogger("org.apache.spark").setLevel(Level.WARN)

  private lazy val sc: SparkContext = new SparkContext(new SparkConf().setAppName("Observatory-TestSuite").setMaster("local[*]"))
  val tolerance = 0.005

  implicit class SparkSeqLike[T: ClassTag](iterable: Seq[T]) {
    def rdd: RDD[T] = sc.parallelize(iterable)
  }
}
