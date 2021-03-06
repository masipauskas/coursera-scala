package observatory

import com.sksamuel.scrimage.Image

/**
  * 3rd milestone: interactive visualization
  */
object Interaction {
  import math._

  /**
    * @param zoom Zoom level
    * @param x X coordinate
    * @param y Y coordinate
    * @return The latitude and longitude of the top-left corner of the tile, as per http://wiki.openstreetmap.org/wiki/Slippy_map_tilenames
    */
  def tileLocation(zoom: Int, x: Int, y: Int): Location = {
    val n = pow(2, zoom)
    val long = x / n * 360 - 180
    val lat = toDegrees(atan(sinh(Pi * (1 - 2 * y / n))))

    Location(lat, long)
  }

  /**
    * @param temperatures Known temperatures
    * @param colors Color scale
    * @param zoom Zoom level
    * @param x X coordinate
    * @param y Y coordinate
    * @return A 256×256 image showing the contents of the tile defined by `x`, `y` and `zooms`
    */
  def tile(temperatures: Iterable[(Location, Double)], colors: Iterable[(Double, Color)], zoom: Int, x: Int, y: Int): Image = {
    import Visualization.predictTemperature
    import observatory.utils.TileVisualizer

    val temperature: (Location) => Double = (location) => predictTemperature(temperatures, location)

    TileVisualizer.tile(temperature, colors, zoom, x, y)
  }

  /**
    * Generates all the tiles for zoom levels 0 to 3 (included), for all the given years.
    * @param yearlyData Sequence of (year, data), where `data` is some data associated with
    *                   `year`. The type of `data` can be anything.
    * @param generateImage Function that generates an image given a year, a zoom level, the x and
    *                      y coordinates of the tile and the data to build the image from
    */
  def generateTiles[Data](
    yearlyData: Iterable[(Int, Data)],
    generateImage: (Int, Int, Int, Int, Data) => Unit
  ): Unit = {
    val zoomLevel = 3

    yearlyData.par.flatMap { case (year, data) =>
      for {
        zoom <- 0 to zoomLevel

        n = pow(2, zoom).toInt

        x <- 0 until n
        y <- 0 until n

      } yield (year, zoom, x, y, data)

    }.foreach{ case (year, zoom, x, y, data) => generateImage(year, zoom, x, y, data) }
  }

}
