package observatory

import scala.collection.parallel.ParSeq

/**
  * 4th milestone: value-added information
  */
object Manipulation {
  import Visualization.predictTemperature

  val grid: ParSeq[Location] = {
    for {
      lat <- -89 to 90
      long <- -180 to 179
    } yield Location(lat, long)
  }.par

  /**
    * @param temperatures Known temperatures
    * @return A function that, given a latitude in [-89, 90] and a longitude in [-180, 179],
    *         returns the predicted temperature at this location
    */
  def makeGrid(temperatures: Iterable[(Location, Double)]): (Int, Int) => Double = {
    val predictedTemperatures = grid
      .map(location => (location, predictTemperature(temperatures, location)))
      .seq.toMap

    (lat, long) => predictedTemperatures(Location(lat,long))
  }

  /**
    * @param temperatures Sequence of known temperatures over the years (each element of the collection
    *                      is a collection of pairs of location and temperature)
    * @return A function that, given a latitude and a longitude, returns the average temperature at this location
    */
  def average(temperatures: Iterable[Iterable[(Location, Double)]]): (Int, Int) => Double = {
    val averages: Map[Location, Double] = if (temperatures.isEmpty) grid.map(location => (location, 0d)).seq.toMap
    else {
      val length = temperatures.size
      val predicted = temperatures.par.map(makeGrid).toSeq

      grid.map(location => (location, predicted.map(predict => predict(location.lat.toInt, location.lon.toInt)).sum / length)).seq.toMap
    }

    (lat, long) => averages(Location(lat, long))
  }

  /**
    * @param temperatures Known temperatures
    * @param normals A grid containing the “normal” temperatures
    * @return A grid containing the deviations compared to the normal temperatures
    */
  def deviation(temperatures: Iterable[(Location, Double)], normals: (Int, Int) => Double): (Int, Int) => Double = {
    val predicted = makeGrid(temperatures)

    (lat, long) => predicted(lat, long) - normals(lat, long)
  }


}

