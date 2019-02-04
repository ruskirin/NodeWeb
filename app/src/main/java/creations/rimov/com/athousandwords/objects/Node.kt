package creations.rimov.com.athousandwords.objects

import creations.rimov.com.athousandwords.WebRenderer
import creations.rimov.com.athousandwords.util.Constants


class Node(center: Shapes.Point, size: Float) : Shapes.Base(center, size) {
    //6 * 7
    private val vertices = FloatArray(WebRenderer.This.POINTS_PER_NODE * WebRenderer.This.NODE_TOTAL_COMPONENTS)

    init {
        initVertices()
    }

    fun getVertices(): FloatArray {
        return vertices
    }

    private fun initVertices() {
        //top left
        vertices[0] = centerX - Constants.RADIUS_CIRCLE
        vertices[1] = centerY + Constants.RADIUS_CIRCLE
        vertices[2] = 1f
        vertices[3] = 0f
        vertices[4] = 0f
        vertices[5] = 0f //s
        vertices[6] = 0f //t
        //bottom left
        vertices[7] = centerX - Constants.RADIUS_CIRCLE
        vertices[8] = centerY - Constants.RADIUS_CIRCLE
        vertices[9] = 1f
        vertices[10] = 0f
        vertices[11] = 0f
        vertices[12] = 0f //s
        vertices[13] = 1f //t
        //bottom right
        vertices[14] = centerX + Constants.RADIUS_CIRCLE
        vertices[15] = centerY - Constants.RADIUS_CIRCLE
        vertices[16] = 1f
        vertices[17] = 0f
        vertices[18] = 0f
        vertices[19] = 1f //s
        vertices[20] = 1f //t
        //top right
        vertices[21] = centerX + Constants.RADIUS_CIRCLE
        vertices[22] = centerY + Constants.RADIUS_CIRCLE
        vertices[23] = 1f
        vertices[24] = 0f
        vertices[25] = 0f
        vertices[26] = 1f //s
        vertices[27] = 0f //t
        //top left
        vertices[28] = centerX - Constants.RADIUS_CIRCLE
        vertices[29] = centerY + Constants.RADIUS_CIRCLE
        vertices[30] = 1f
        vertices[31] = 0f
        vertices[32] = 0f
        vertices[33] = 0f //s
        vertices[34] = 0f //t
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