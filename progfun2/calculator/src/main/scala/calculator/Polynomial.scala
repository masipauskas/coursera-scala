package calculator

object Polynomial {
  def computeDelta(a: Signal[Double], b: Signal[Double],
      c: Signal[Double]): Signal[Double] = {
    def solveDelta(a: Double, b: Double, c: Double) = (b * b) - (4 * a * c)

    Var(solveDelta(a(), b(), c()))
  }

  def computeSolutions(a: Signal[Double], b: Signal[Double],
      c: Signal[Double], delta: Signal[Double]): Signal[Set[Double]] = {
    def computeSolutions(a: Double, b: Double, c: Double, delta: Double): Set[Double] = {
      def computeSolution(a: Double, b: Double, c: Double, delta: Double, sign: Int) =
        ((-b) + sign * math.sqrt(delta)) / 2 * a

      if (delta >= 0) Set(computeSolution(a, b, c, delta, 1), computeSolution(a, b, c, delta, -1))
      else Set.empty
    }

    Var(computeSolutions(a(), b(), c(), delta()))
  }
}
