package observatory

import java.time.LocalDate

import org.apache.spark.rdd.RDD

/**
  * 1st milestone: data extraction
  */
object Extraction {

  /**
    * @param year             Year number
    * @param stationsFile     Path of the stations resource file to use (e.g. "/stations.csv")
    * @param temperaturesFile Path of the temperatures resource file to use (e.g. "/1975.csv")
    * @return A sequence containing triplets (date, location, temperature)
    */
  def locateTemperatures(year: Int, stationsFile: String, temperaturesFile: String): Iterable[(LocalDate, Location, Double)] = {
    ???
  }

  def locateTemperaturesRDD(year: Int, stations: RDD[String], temperatures: RDD[String]): RDD[(LocalDate, Location, Double)] = {
    import observatory.utils.Parser._
    val validStations = stations
      .map(parseStation)
      .filter(isValid)
      .map(opt => opt.get)

    val validTemperatures = temperatures
      .map(parseObservation(_, year))
      .filter(isValid)
      .map(opt => (opt.get._1, (opt.get._2, opt.get._3)))

    validStations
      .join(validTemperatures)
      .mapValues { case (location, (date, temperature)) => (date, location, temperature) }
      .values
  }

  /**
    * @param records A sequence containing triplets (date, location, temperature)
    * @return A sequence containing, for each location, the average temperature over the year.
    */
  def locationYearlyAverageRecords(records: Iterable[(LocalDate, Location, Double)]): Iterable[(Location, Double)] = {
    ???
  }

}
