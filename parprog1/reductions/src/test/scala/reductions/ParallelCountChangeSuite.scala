package reductions

import java.util.concurrent._

import scala.collection._
import org.scalatest.FunSuite
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import common._
import ParallelCountChange.{countChange, _}

@RunWith(classOf[JUnitRunner])
class ParallelCountChangeSuite extends FunSuite {
  private def countChangeShouldReturn0ForNegativeMoney(countChange: (Int, List[Int]) => Int) = {
    def check(money: Int, coins: List[Int]) =
      assert(countChange(money, coins) == 0,
        s"countChang($money, _) should be 0")

    check(-1, List())
    check(-1, List(1, 2, 3))
    check(-Int.MinValue, List())
    check(-Int.MinValue, List(1, 2, 3))
  }

  private def countChangeShouldReturn1WhenNoMoney(countChange: (Int, List[Int]) => Int) = {
    def check(coins: List[Int]) =
      assert(countChange(0, coins) == 1,
        s"countChang(0, _) should be 1")

    check(List())
    check(List(1, 2, 3))
    check(List.range(1, 100))
  }

  private def countChangeShouldReturn0ForMoneyAndNoCounts(countChange: (Int, List[Int]) => Int) = {
    def check(money: Int) =
      assert(countChange(money, List()) == 0,
        s"countChang($money, List()) should be 0")

    check(1)
    check(Int.MaxValue)
  }

  private def countChangeShouldWorkWhenThereIsOneCoin(countChange: (Int, List[Int]) => Int) = {
    def check(money: Int, coins: List[Int], expected: Int) =
      assert(countChange(money, coins) == expected,
        s"countChange($money, $coins) should be $expected")

    check(1, List(1), 1)
    check(2, List(1), 1)
    check(1, List(2), 0)
    check(Int.MaxValue, List(Int.MaxValue), 1)
    check(Int.MaxValue - 1, List(Int.MaxValue), 0)
  }

  private def countChangeShouldWorkWhenThereAreMultipleCoins(countChange: (Int, List[Int]) => Int) = {
    def check(money: Int, coins: List[Int], expected: Int) =
      assert(countChange(money, coins) == expected,
        s"countChange($money, $coins) should be $expected")

    check(50, List(1, 2, 5, 10), 341)
    check(250, List(1, 2, 5, 10, 20, 50), 177863)
  }

  test("countChange should return 0 for money < 0") {
    countChangeShouldReturn0ForNegativeMoney(countChange)
  }

  test("countChange should return 1 when money == 0") {
    countChangeShouldReturn1WhenNoMoney(countChange)
  }

  test("countChange should return 0 for money > 0 and coins = List()") {
    countChangeShouldReturn0ForMoneyAndNoCounts(countChange)
  }

  test("countChange should work when there is only one coin") {
    countChangeShouldWorkWhenThereIsOneCoin(countChange)
  }

  test("countChange should work for multi-coins") {
    countChangeShouldWorkWhenThereAreMultipleCoins(countChange)
  }

  test("parCountChange with combinedThreshold should return 0 for money < 0") {
    countChangeShouldReturn0ForNegativeMoney((m, c) => parCountChange(m, c, combinedThreshold(m, c)))
  }

  test("parCountChange with combinedThreshold should return 1 when money == 0") {
    countChangeShouldReturn1WhenNoMoney((m, c) => parCountChange(m, c, combinedThreshold(m, c)))
  }

  test("parCountChange with combinedThreshold should return 0 for money > 0 and coins = List()") {
    countChangeShouldReturn0ForMoneyAndNoCounts((m, c) => parCountChange(m, c, combinedThreshold(m, c)))
  }

  test("parCountChange with combinedThreshold should work when there is only one coin") {
    countChangeShouldWorkWhenThereIsOneCoin((m, c) => parCountChange(m, c, combinedThreshold(m, c)))
  }

  test("parCountChange with combinedThreshold should work for multi-coins") {
    countChangeShouldWorkWhenThereAreMultipleCoins((m, c) => parCountChange(m, c, combinedThreshold(m, c)))
  }

  test("parCountChange with totalCoinsThreshold should return 0 for money < 0") {
    countChangeShouldReturn0ForNegativeMoney((m, c) => parCountChange(m, c, totalCoinsThreshold(c.length)))
  }

  test("parCountChange with totalCoinsThreshold should return 1 when money == 0") {
    countChangeShouldReturn1WhenNoMoney((m, c) => parCountChange(m, c, totalCoinsThreshold(c.length)))
  }

  test("parCountChange with totalCoinsThreshold should return 0 for money > 0 and coins = List()") {
    countChangeShouldReturn0ForMoneyAndNoCounts((m, c) => parCountChange(m, c, totalCoinsThreshold(c.length)))
  }

  test("parCountChange with totalCoinsThreshold should work when there is only one coin") {
    countChangeShouldWorkWhenThereIsOneCoin((m, c) => parCountChange(m, c, totalCoinsThreshold(c.length)))
  }

  test("parCountChange with totalCoinsThreshold should work for multi-coins") {
    countChangeShouldWorkWhenThereAreMultipleCoins((m, c) => parCountChange(m, c, totalCoinsThreshold(c.length)))
  }

  test("parCountChange with moneyThreshold should return 0 for money < 0") {
    countChangeShouldReturn0ForNegativeMoney((m, c) => parCountChange(m, c, moneyThreshold(m)))
  }

  test("parCountChange with moneyThreshold should return 1 when money == 0") {
    countChangeShouldReturn1WhenNoMoney((m, c) => parCountChange(m, c, moneyThreshold(m)))
  }

  test("parCountChange with moneyThreshold should return 0 for money > 0 and coins = List()") {
    countChangeShouldReturn0ForMoneyAndNoCounts((m, c) => parCountChange(m, c, moneyThreshold(m)))
  }

  test("parCountChange with moneyThreshold should work when there is only one coin") {
    countChangeShouldWorkWhenThereIsOneCoin((m, c) => parCountChange(m, c, moneyThreshold(m)))
  }

  test("parCountChange with moneyThreshold should work for multi-coins") {
    countChangeShouldWorkWhenThereAreMultipleCoins((m, c) => parCountChange(m, c, moneyThreshold(m)))
  }
}
