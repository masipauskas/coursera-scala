package reductions

import scala.annotation._
import org.scalameter._
import common._

object ParallelParenthesesBalancingRunner {

  @volatile var seqResult = false

  @volatile var parResult = false

  val standardConfig = config(
    Key.exec.minWarmupRuns -> 40,
    Key.exec.maxWarmupRuns -> 80,
    Key.exec.benchRuns -> 120,
    Key.verbose -> true
  ) withWarmer(new Warmer.Default)

  def main(args: Array[String]): Unit = {
    val length = 100000000
    val chars = new Array[Char](length)
    val threshold = 10000
    val seqtime = standardConfig measure {
      seqResult = ParallelParenthesesBalancing.balance(chars)
    }
    println(s"sequential result = $seqResult")
    println(s"sequential balancing time: $seqtime ms")

    val fjtime = standardConfig measure {
      parResult = ParallelParenthesesBalancing.parBalance(chars, threshold)
    }
    println(s"parallel result = $parResult")
    println(s"parallel balancing time: $fjtime ms")
    println(s"speedup: ${seqtime / fjtime}")
  }
}

object ParallelParenthesesBalancing {
  private def weight(char: Char) = char match {
    case '(' => 1
    case ')' => -1
    case _ => 0
  }

  /** Returns `true` iff the parentheses in the input `chars` are balanced.
   */
  def balance(chars: Array[Char]): Boolean = {
    @tailrec
    def balance0(index: Int, until: Int, acc: Int): Boolean = {
      if (index == until) acc == 0
      else if (acc < 0) false
      else balance0(index + 1, until, acc + weight(chars(index)))
    }

    balance0(0, chars.length, 0)
  }

  /** Returns `true` iff the parentheses in the input `chars` are balanced.
   */
  def parBalance(chars: Array[Char], threshold: Int): Boolean = {

    @tailrec
    def traverse(idx: Int, until: Int, openParenthesisCount: Int, closedParenthesisCount: Int): (Int, Int) = {
      if (idx == until) (openParenthesisCount, closedParenthesisCount)
      else {
        chars(idx) match {
          case '(' => traverse(idx + 1, until, openParenthesisCount + 1, closedParenthesisCount)
          case ')' => traverse(idx + 1, until, openParenthesisCount, closedParenthesisCount + 1)
          case _ => traverse(idx + 1, until, openParenthesisCount, closedParenthesisCount)
        }
      }
    }

    def reduce(from: Int, until: Int): (Int, Int) = {
      val length = until - from
      if (length <= threshold) traverse(from, until, 0, 0)
      else {
        val pivot = length / 2
        val ((lOpen, lClosed), (rOpen, rClosed)) = parallel(reduce(from, from + pivot), reduce(from + pivot, until))
        if (lOpen > rClosed) (lOpen - rClosed + rOpen, lClosed)
        else (rOpen, rClosed + lClosed - lOpen)
      }
    }

    reduce(0, chars.length) == (0, 0)
  }
}
