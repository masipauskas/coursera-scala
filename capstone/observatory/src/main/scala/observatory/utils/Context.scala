package observatory.utils

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

trait Context {
  import org.apache.log4j.{Level, Logger}
  Logger.getLogger("org.apache.spark").setLevel(Level.WARN)

  lazy val context: SparkContext = Context.sc

  def readFile(file: String): RDD[String] = {
    context.textFile(getClass.getResource(file).toString)
  }
}

object Context {
  lazy val sc: SparkContext = new SparkContext(new SparkConf().setAppName("Observatory").setMaster("local[*]"))
}
