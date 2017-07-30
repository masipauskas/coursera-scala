package observatory

import com.sksamuel.scrimage.{Image, Pixel}

/**
  * 2nd milestone: basic visualization
  */
object Visualization {

  /**
    * @param temperatures Known temperatures: pairs containing a location and the temperature at this location
    * @param location Location where to predict the temperature
    * @return The predicted temperature at `location`
    */
  def predictTemperature(temperatures: Iterable[(Location, Double)], location: Location): Double = {
    import observatory.utils.GreatCircleDistance.distance
    import Math._
    val power = 2

    def weight(distance: Double) = 1 / pow(distance, power)

    val distancesWithTemperature = temperatures
      .map { case(l, t) => (distance(l, location), t) }
      .toSeq
      .sortBy(_._1)

    if (distancesWithTemperature.head._1 <= 1000) distancesWithTemperature.head._2 //if there is a location which is close enough (<1km) - use it
    else {
      val (temperature, weights) = distancesWithTemperature.foldLeft((0d, 0d)) { case ((predicted, w), (distance, t)) => (predicted + weight(distance) * t, w + weight(distance)) }
      temperature / weights
    }
  }

  /**
    * @param points Pairs containing a value and its associated color
    * @param value The value to interpolate
    * @return The color that corresponds to `value`, according to the color scale defined by `points`
    */
  def interpolateColor(points: Iterable[(Double, Color)], value: Double): Color = {
    def interpolate(from: Int, to: Int, normalized: Double): Int = {
      ((1 - normalized) * from + normalized * to).round.toInt
    }
    val colors = points.toSeq.sortBy(_._1)
    val partitioned = colors.partition(_._1 <= value)

    val lowerColor = partitioned._1.lastOption
    val higherColor = partitioned._2.headOption

    if (lowerColor.isEmpty) higherColor.map(_._2).get
    else if (higherColor.isEmpty) lowerColor.map(_._2).get
    else {
      val (hBound, higher) = higherColor.get
      val (lBound, lower) = lowerColor.get

      val normalized = (value - lBound) / (hBound - lBound)
      Color(
        interpolate(lower.red, higher.red, normalized),
        interpolate(lower.green, higher.green, normalized),
        interpolate(lower.blue, higher.blue, normalized)
      )
    }
  }

  /**
    * @param temperatures Known temperatures
    * @param colors Color scale
    * @return A 360×180 image where each pixel shows the predicted temperature at its location
    */
  def visualize(temperatures: Iterable[(Location, Double)], colors: Iterable[(Double, Color)]): Image = {
    val width = 360
    val height = 180

    val latitudeOffset = -height/2
    val longitudeOffset = -width/2

    val pixels = for {
      x <- 0 until width
      y <- 0 until height

      temperature = predictTemperature(temperatures, Location(y + latitudeOffset, x + longitudeOffset))
      color = interpolateColor(colors, temperature)
    } yield Pixel(color.red, color.green, color.blue, 255)


    Image(360, 180, pixels.toArray)
  }

}

