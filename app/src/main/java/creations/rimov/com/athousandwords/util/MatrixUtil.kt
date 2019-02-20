package creations.rimov.com.athousandwords.util

import android.util.Log

object MatrixUtil {

    /*@JvmStatic
    fun createOrthoMatrix(width: Int, height: Int, projectionMatrix: FloatArray) {

        val aspectRatio: Float =
            if (width > height)
                width.toFloat() / height.toFloat()
            else
                height.toFloat() / width.toFloat()
        Log.i("createOrthoMat", "Aspect ratio = $aspectRatio, width = $width, height = $height")

        if(width > height)
            Matrix.orthoM(projectionMatrix, 0, -aspectRatio, aspectRatio, -1f, 1f, -1f, 1f)
        else
            Matrix.orthoM(projectionMatrix, 0, -1f, 1f, -aspectRatio, aspectRatio, -1f, 1f)

        for(i in projectionMatrix.indices) {
            Log.i("createOrthoMat", "Proj Matrix: entry $i = ${projectionMatrix[i]}")
        }
    }*/

    @JvmStatic
    fun createOrthoMatrix(width: Int, height: Int, normalizeMatrix: FloatArray) {

        normalizeMatrix[0] = 2.0f / width.toFloat() //X
        normalizeMatrix[1] = 0.0f
        normalizeMatrix[2] = 0.0f
        normalizeMatrix[3] = 0.0f
        normalizeMatrix[4] = 0.0f
        normalizeMatrix[5] = -2.0f / height.toFloat() //Y
        normalizeMatrix[6] = 0.0f
        normalizeMatrix[7] = 0.0f
        normalizeMatrix[8] = 0.0f
        normalizeMatrix[9] = 0.0f
        normalizeMatrix[10] = 1.0f //Z
        normalizeMatrix[11] = 0.0f
        normalizeMatrix[12] = -1.0f //X translation
        normalizeMatrix[13] = 1.0f  //Y translation
        normalizeMatrix[14] = 0.0f  //Z translation
        normalizeMatrix[15] = 1.0f  //W
    }
}