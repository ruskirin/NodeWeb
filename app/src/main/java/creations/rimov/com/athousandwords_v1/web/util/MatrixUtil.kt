package creations.rimov.com.athousandwords_v1.web.util

object MatrixUtil {

    @JvmStatic
    fun orthoTransform(width: Int, height: Int, normalizeMatrix: FloatArray) {

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

    //Not used atm
    @JvmStatic
    fun scaleTransform(scaleX: Float, scaleY: Float, scaleZ: Float, matrix: FloatArray) {

        matrix[0] = 1.0f * scaleX //X
        matrix[1] = 0.0f
        matrix[2] = 0.0f
        matrix[3] = 0.0f
        matrix[4] = 0.0f
        matrix[5] = 1.0f * scaleY //Y
        matrix[6] = 0.0f
        matrix[7] = 0.0f
        matrix[8] = 0.0f
        matrix[9] = 0.0f
        matrix[10] = 1.0f * scaleZ //Z
        matrix[11] = 0.0f
        matrix[12] = 0.0f //X translation
        matrix[13] = 0.0f  //Y translation
        matrix[14] = 0.0f  //Z translation
        matrix[15] = 1.0f  //W
    }
}