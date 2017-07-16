package stackoverflow

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

import scala.reflect.ClassTag

trait SparkHelpers {
  @transient lazy val conf: SparkConf = new SparkConf().setMaster("local").setAppName("StackOverflowTest")
  @transient lazy val sc: SparkContext = new SparkContext(conf)

  def rdd[T : ClassTag](vals: Traversable[T]): RDD[T] = sc.parallelize(vals.toList)
  def rdd[T : ClassTag](vals: T*): RDD[T] = rdd(vals)

  def equals[T](actual: Iterable[T], expected: Iterable[T]): Unit = {
    val equals = actual.zip(expected).forall(pair => pair._1 == pair._2)
    assert(equals, s"Expected: $expected, got: $actual")
  }
}
