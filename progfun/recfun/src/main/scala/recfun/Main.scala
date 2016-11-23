package recfun

import scala.annotation.tailrec

object Main {
  def main(args: Array[String]) {
    println("Pascal's Triangle")
    for (row <- 0 to 10) {
      for (col <- 0 to row)
        print(pascal(col, row) + " ")
      println()
    }
  }

  /**
   * Exercise 1
   */
    def pascal(c: Int, r: Int): Int = {
      if (c == 0 || c == r) 1
      else pascal(c - 1, r - 1) + pascal(c, r - 1)
    }
  
  /**
   * Exercise 2
   */
    def balance(chars: List[Char]): Boolean = {
      @tailrec
      def balance0(chars: List[Char], acc: Int): Boolean = {
        if (acc < 0) false // non-balanced if accumulator is negative
        else if (chars.isEmpty) acc == 0 // if end of the list check if accumulator is 0 (balanced)
        else {
          val increment = chars.head match {
            case '(' => 1
            case ')' => -1
            case _ => 0
          }

          balance0(chars.tail, acc + increment)
        }
      }

      balance0(chars, 0)
    }
  
  /**
   * Exercise 3 - Implementation based on https://mitpress.mit.edu/sicp/full-text/book/book-Z-H-11.html#%_idx_722
   */
    def countChange(money: Int, coins: List[Int]): Int = {
      def countChange0(money: Int, coins: List[Int]): Int = {
        if (coins.isEmpty || money <0 ) 0
        else if (money == 0) 1
        else {
          countChange0(money, coins.tail) + countChange0(money - coins.head, coins)
        }
      }

      countChange0(money, coins.sorted(Ordering.Int.reverse))
    }
 }
