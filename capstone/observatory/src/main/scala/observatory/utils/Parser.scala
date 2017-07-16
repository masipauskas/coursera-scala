package observatory.utils

import java.time.LocalDate

import observatory.Location

import scala.util.Try


object Parser {
  private def tokenise(line: String): Array[String] = line.split(",")

  private def valueOrEmpty(entries: Array[String], idx: Int) = {
    if (entries.length - 1 < idx) ""
    else entries(idx).trim
  }

  private def parseEntry[T](entries: Array[String], idx: Int, parser: String => T): Option[T] = {
    val value = valueOrEmpty(entries, idx)

    if (value.isEmpty) None
    else Try(parser(value)).toOption
  }

  private def parseEntry(entries: Array[String], idx: Int): Option[String] = {
    parseEntry(entries, idx, v => v)
  }

  def isValid[T](entry: Option[T]): Boolean = entry.isDefined

  private def stationKey(stn: String, wban: String) = s"${stn.trim}${wban.trim}"

  def parseStation(line: String): Option[(String, Location)] = {
    val components = tokenise(line)

    val location = for {
      lat <- parseEntry(components, 2, _.toDouble)
      long <- parseEntry(components, 3, _.toDouble)
    } yield Location(lat, long)

    val key = stationKey(valueOrEmpty(components, 0), valueOrEmpty(components, 1))

    location.map { l => (key, l)}
  }

  def parseObservation(line: String, year: Int): Option[(String, LocalDate, Double)] = {
    import Temperature.fahrenheitToCelsius

    val components = tokenise(line)
    val key = stationKey(valueOrEmpty(components, 0), valueOrEmpty(components, 1))

    for {
      month <- parseEntry(components, 2, _.toInt)
      day <- parseEntry(components, 3, _.toInt)
      fahrenheit <- parseEntry(components, 4, _.toDouble)

      if fahrenheit != 9999

      celsius = fahrenheitToCelsius(fahrenheit)
      date = LocalDate.of(year, month, day)
    } yield (key, date, celsius)
  }
}
