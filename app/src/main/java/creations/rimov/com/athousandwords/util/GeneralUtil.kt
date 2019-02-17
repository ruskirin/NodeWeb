package creations.rimov.com.athousandwords.util

import android.opengl.Matrix
import android.util.Log
import creations.rimov.com.athousandwords.objects.Shapes

object GeneralUtil {

    @JvmStatic
    fun screenToNormalized(center: Shapes.Point, widthPhone: Int, heightPhone: Int): Shapes.Point {
        return Shapes.Point(
            ((center.x * 2f) / widthPhone.toFloat()) - 1f,
            -((center.y * 2f) / heightPhone.toFloat() - 1f))
    }
}