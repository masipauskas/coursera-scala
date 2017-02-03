package quickcheck

import common._

import org.scalacheck._
import Arbitrary._
import Gen._
import Prop._

abstract class QuickCheckHeap extends Properties("Heap") with IntHeap {

  lazy val genHeap: Gen[H] = for {
    value <- arbitrary[A]
    heap <- oneOf(const(empty), genHeap)
  } yield insert(value, heap)

  implicit lazy val arbHeap: Arbitrary[H] = Arbitrary(genHeap)

  property("gen1") = forAll { (h: H) =>
    val m = if (isEmpty(h)) 0 else findMin(h)
    findMin(insert(m, h)) == m
  }

  property("insertAndRemoveShouldResultInEmptyHeap") = forAll { (value: A) =>
    deleteMin(insert(value, empty)) == empty
  }

  property("insertTwoElementsIntoTheHeapFindMinShouldFindSmallestElement") = forAll { (first: A, second: A) =>
    val min = if (first > second) second else first
    findMin(insert(second, insert(first, empty))) == min
  }

  property("findingAndRemovingTheMinimumOfTheHeapShouldReturnSortedListOfElements") = forAll { (heap: H) =>
    def elements(h: H, acc: List[A]): List[A] = {
      if (isEmpty(h)) acc
      else {
        val min = findMin(h)
        elements(deleteMin(h), min :: acc)
      }
    }

    val all = elements(heap, List.empty)
    all.sorted == all.reverse
  }

  property("meldingTwoHeapsShouldResultInAHeapWithCorrectMinimum") = forAll { (first: H, second: H) =>
    def minWithFallback(first: H, second: H) = if (!isEmpty(first)) findMin(first) else findMin(second)

    if (isEmpty(first) && isEmpty(second)) isEmpty(meld(first, second))
    else {
      val firstMin = minWithFallback(first, second)
      val secondMin = minWithFallback(second, first)

      val min = if (firstMin < secondMin) firstMin else secondMin

      findMin(meld(first, second)) == min
    }
  }
}
