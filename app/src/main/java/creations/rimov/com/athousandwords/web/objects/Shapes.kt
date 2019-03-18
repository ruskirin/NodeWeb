package creations.rimov.com.athousandwords.web.objects


object Shapes {

    class Point(val x: Float, val y: Float)

    abstract class Base(center: Point) {
        var centerX: Float = center.x
        var centerY: Float = center.y

        abstract fun translateX(dX: Float)

        abstract fun translateY(dY: Float)
    }
}