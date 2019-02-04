package creations.rimov.com.athousandwords.util

import android.opengl.Matrix
import creations.rimov.com.athousandwords.objects.Shapes

object GeneralUtil {

    @JvmStatic
    fun createOrthoProjection(width: Int, height: Int, projectionMatrix: FloatArray) {
        val aspectRatio: Float =
            if (width > height)
                width.toFloat() / height.toFloat()
            else
                height.toFloat() / width.toFloat()

        if(width > height)
            Matrix.orthoM(projectionMatrix, 0, -aspectRatio, aspectRatio, -1f, 1f, -1f, 1f)
        else
            Matrix.orthoM(projectionMatrix, 0, -1f, 1f, -aspectRatio, aspectRatio, -1f, 1f)
    }

    @JvmStatic
    fun pixelsToPoint(x: Float, y: Float, widthPhone: Int, heightPhone: Int): Shapes.Point {
        return Shapes.Point(
            ((x * 2f) / widthPhone.toFloat()) - 1f,
            -((y * 2f) / heightPhone.toFloat() - 1f))
    }
}