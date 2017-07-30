package observatory.utils

import observatory.Location

object GreatCircleDistance {
  def distance(start: Location, end: Location) = {
    import Math._

    val startLatitude = start.lat.toRadians
    val endLatitude = end.lat.toRadians
    val startLongitude = start.lon.toRadians
    val endLongitude = end.lon.toRadians

    val angle = acos(sin(startLatitude) * sin(endLatitude) + cos(startLatitude) * cos(endLatitude) * cos(startLongitude - endLongitude))

    angle * 6372.8 * 1000 //see: https://en.wikipedia.org/wiki/Great-circle_distance - on the best radius of earth to use for the formula
  }
}
