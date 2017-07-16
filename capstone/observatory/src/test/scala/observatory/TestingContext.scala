package observatory

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

import scala.reflect.ClassTag

trait TestingContext {
  private lazy val sc: SparkContext = new SparkContext(new SparkConf().setAppName("Observatory-TestSuite"))

  implicit class SparkSeqLike[T: ClassTag](iterable: Seq[T]) {
    def rdd: RDD[T] = sc.parallelize(iterable)
  }
}
