package observatory.utils

import com.sksamuel.scrimage.{Image, Pixel}
import observatory.{Color, Interaction, Location, Visualization}

import scala.math.abs

object TileVisualizer {
  def tile(
            value: (Location) => Double,
            colors: Iterable[(Double, Color)],
            zoom: Int,
            x: Int,
            y: Int
          ): Image = {
    import Interaction.tileLocation
    import Visualization._
    val side = 256

    def step(start: Double, end:Double) = abs((start - end) / side)

    val top = tileLocation(zoom, x, y)
    val bottom = tileLocation(zoom, x + 1, y + 1)

    val latitudeStep: Double = step(top.lat, bottom.lat)
    val longitudeStep: Double = step(top.lon, bottom.lon)

    def lat(s: Double) = top.lat + s * latitudeStep
    def lon(s: Double) = top.lon + s * longitudeStep

    val pixels = for {
      x <- 0 until side
      y <- 0 until side

      valueAtLocation = value(Location(lat(y), lon(x)))
      color = interpolateColor(colors, valueAtLocation)
    } yield Pixel(color.red, color.green, color.blue, 127)

    Image(side, side, pixels.toArray)
  }
}
