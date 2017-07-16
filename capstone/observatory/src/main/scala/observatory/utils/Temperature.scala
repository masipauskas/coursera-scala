package observatory.utils

object Temperature {
  def fahrenheitToCelsius(temperature: Double): Double = {
    (temperature - 32) / 9 * 5
  }
}
