package scalashop

import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class PartitionerSuite extends FunSuite {
  import Partitioner.partition

  test("partitioner partitions even number of partitions correctly") {
    assert(partition(0, 10, 2) === Seq((0, 5), (5, 10)))
  }

  test("partitioner partitions odd number of partitions correctly") {
    assert(partition(0, 9, 2) === Seq((0,4), (4,8), (8,9)))
  }

  test("partitioner partitions correctly into range of step 1") {
    assert(partition(0, 3, 3) === Seq((0,1), (1,2), (2, 3)))
  }
}
