package creations.rimov.com.athousandwords.objects


object Shapes {

    class Point(val x: Float, val y: Float) { }

    abstract class Base(center: Point, var size: Float) {
        var centerX: Float = center.x
        var centerY: Float = center.y

        abstract fun translateX(dX: Float)

        abstract fun translateY(dY: Float)

        abstract fun zoom(dSize: Float)
    }
}