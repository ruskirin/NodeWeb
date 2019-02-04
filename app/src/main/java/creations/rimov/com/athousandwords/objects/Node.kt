package creations.rimov.com.athousandwords.objects

import creations.rimov.com.athousandwords.WebRenderer
import creations.rimov.com.athousandwords.util.Constants


class Node(center: Shapes.Point, size: Float) : Shapes.Base(center, size) {
    //6 * 5
    private val vertices = FloatArray(WebRenderer.This.POINTS_PER_NODE * WebRenderer.This.NODE_TOTAL_COMPONENTS)

    init {
        initVertices()
    }

    fun getVertices(): FloatArray {
        return vertices
    }

    private fun initVertices() {
        //center
        vertices[0] = centerX
        vertices[1] = centerY
        vertices[2] = 1f
        vertices[3] = 0f
        vertices[4] = 0f
        //top left
        vertices[5] = centerX - Constants.RADIUS_CIRCLE
        vertices[6] = centerY + Constants.RADIUS_CIRCLE
        vertices[7] = 1f
        vertices[8] = 0f
        vertices[9] = 0f
        //bottom left
        vertices[10] = centerX - Constants.RADIUS_CIRCLE
        vertices[11] = centerY - Constants.RADIUS_CIRCLE
        vertices[12] = 1f
        vertices[13] = 0f
        vertices[14] = 0f
        //bottom right
        vertices[15] = centerX + Constants.RADIUS_CIRCLE
        vertices[16] = centerY - Constants.RADIUS_CIRCLE
        vertices[17] = 1f
        vertices[18] = 0f
        vertices[19] = 0f
        //top right
        vertices[20] = centerX + Constants.RADIUS_CIRCLE
        vertices[21] = centerY + Constants.RADIUS_CIRCLE
        vertices[22] = 1f
        vertices[23] = 0f
        vertices[24] = 0f

        vertices[25] = centerX - Constants.RADIUS_CIRCLE
        vertices[26] = centerY + Constants.RADIUS_CIRCLE
        vertices[27] = 1f
        vertices[28] = 0f
        vertices[29] = 0f
    }

    override fun translateX(dX: Float) {
        centerX += dX
        initVertices()
    }

    override fun translateY(dY: Float) {
        centerY += dY
        initVertices()
    }

    override fun zoom(dSize: Float) {

    }
}