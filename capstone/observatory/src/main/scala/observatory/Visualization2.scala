package observatory

import com.sksamuel.scrimage.Image

/**
  * 5th milestone: value-added information visualization
  */
object Visualization2 {

  /**
    * @param x X coordinate between 0 and 1
    * @param y Y coordinate between 0 and 1
    * @param d00 Top-left value
    * @param d01 Bottom-left value
    * @param d10 Top-right value
    * @param d11 Bottom-right value
    * @return A guess of the value at (x, y) based on the four known values, using bilinear interpolation
    *         See https://en.wikipedia.org/wiki/Bilinear_interpolation#Unit_Square
    */
  def bilinearInterpolation(
    x: Double,
    y: Double,
    d00: Double,
    d01: Double,
    d10: Double,
    d11: Double
  ): Double = {
    UnitSquare.interpolate(UnitSquare(x, y, d00, d01, d10, d11))
  }

  /**
    * @param grid Grid to visualize
    * @param colors Color scale to use
    * @param zoom Zoom level of the tile to visualize
    * @param x X value of the tile to visualize
    * @param y Y value of the tile to visualize
    * @return The image of the tile at (x, y, zoom) showing the grid using the given color scale
    */
  def visualizeGrid(
    grid: (Int, Int) => Double,
    colors: Iterable[(Double, Color)],
    zoom: Int,
    x: Int,
    y: Int
  ): Image = {
    import observatory.utils.TileVisualizer.tile

    val temperature: Location => Double = (location) => {
      val square = UnitSquare.calculate(location, grid)
      UnitSquare.interpolate(square)
    }

    tile(temperature, colors, zoom, x, y)
  }



}

case class UnitSquare(x: Double, y: Double, d00: Double, d01: Double, d10: Double, d11: Double)

object UnitSquare {
  def interpolate(data: UnitSquare): Double = {
    data.d00 * (1 - data.x) * (1 - data.y) + data.d10 * data.x * (1 - data.y) + data.d01 * (1 - data.x) * data.y + data.d11 * data.x * data.y
  }

  def calculate(location: Location, grid: (Int, Int) => Double): UnitSquare = {
    import math._

    val topLeftLatitude = ceil(location.lat).toInt
    val topLeftLongitude = ceil(location.lon).toInt
    val bottomRightLatitude = floor(location.lat).toInt
    val bottomRightLongitude = floor(location.lon).toInt

    val longitude = location.lon - bottomRightLongitude
    val latitude = location.lat - topLeftLatitude

    UnitSquare(longitude, latitude,
      grid(topLeftLatitude, topLeftLongitude),
      grid(bottomRightLatitude, topLeftLongitude),
      grid(topLeftLatitude, bottomRightLongitude),
      grid(bottomRightLatitude, bottomRightLongitude)
    )
  }
}
