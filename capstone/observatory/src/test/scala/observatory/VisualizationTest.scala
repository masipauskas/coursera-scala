package observatory


import com.sksamuel.scrimage.Image
import org.scalatest.prop.Checkers
import org.scalatest.{FunSuite, Matchers}

trait VisualizationTest extends FunSuite with Checkers with Matchers {
  import Visualization._

  private val colorScale = List((-1.0,Color(255,0,0)), (0.0,Color(0,0,255)))

  private val locationAverages = Seq(
    (Location(37.35, -78.433), 27.3),
    (Location(37.358, -78.438), 1.0)
  )

  test("interpolateColor should predict color correctly") {
    interpolateColor(colorScale, -0.75) shouldEqual Color(191,0,64)
  }

  test("interpolateColor should return lowest color if out of bounds on lower end") {
    interpolateColor(colorScale, -2) shouldEqual Color(255,0,0)
  }

  test("interpolateColor should return highest color if out of bounds on higher end") {
    interpolateColor(colorScale, 1) shouldEqual Color(0,0,255)
  }

  test("visualize should execute successfully") {
    visualize(locationAverages, colorScale) shouldNot be(null)
  }
}
