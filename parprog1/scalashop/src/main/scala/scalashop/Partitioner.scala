package scalashop

object Partitioner {
  def partition(from: Int, to: Int, partitions: Int): Seq[(Int, Int)] = {
    val width = to - from
    val step: Int =  width / partitions

    val steps = (from to to) by step
    val ranges = steps.sliding(2).map(pair => (pair(0), pair(1))).toSeq

    val lastStep = to - to % step
    val lastStepOfSet = to - lastStep

    if (lastStepOfSet != 0) ranges :+ (lastStep, to) else ranges
  }
}
