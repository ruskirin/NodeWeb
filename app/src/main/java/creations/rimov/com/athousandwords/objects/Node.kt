package creations.rimov.com.athousandwords.objects

import android.util.Log
import creations.rimov.com.athousandwords.WebRenderer
import creations.rimov.com.athousandwords.util.Constants


class Node(center: Shapes.Point) : Shapes.Base(center) {
    //6 * 7
    private val vertices = FloatArray(WebRenderer.This.POINTS_PER_NODE * WebRenderer.This.NODE_TOTAL_COMPONENTS)

    var texture: Int = WebRenderer.This.NODE_TEXTURE_EMPTY
        set(value) {
            if(value in WebRenderer.This.NODE_TEXTURE_RES_ARRAY)
                field = value
            else
                Log.e("Node.class", "Attempt to set unknown texture: $value")
        }

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
        vertices[2] = 0f
        vertices[3] = 1f
        vertices[4] = 1f
        vertices[5] = 0.5f //s
        vertices[6] = 0.5f //t
        //top left
        vertices[7] = centerX - Constants.RADIUS_CIRCLE
        vertices[8] = centerY + Constants.RADIUS_CIRCLE
        vertices[9] = 0f
        vertices[10] = 1f
        vertices[11] = 0f
        vertices[12] = 0f //s
        vertices[13] = 0f //t
        //bottom left
        vertices[14] = centerX - Constants.RADIUS_CIRCLE
        vertices[15] = centerY - Constants.RADIUS_CIRCLE
        vertices[16] = 0f
        vertices[17] = 1f
        vertices[18] = 0f
        vertices[19] = 0f //s
        vertices[20] = 1f //t
        //bottom right
        vertices[21] = centerX + Constants.RADIUS_CIRCLE
        vertices[22] = centerY - Constants.RADIUS_CIRCLE
        vertices[23] = 0f
        vertices[24] = 1f
        vertices[25] = 0f
        vertices[26] = 1f //s
        vertices[27] = 1f //t
        //top right
        vertices[28] = centerX + Constants.RADIUS_CIRCLE
        vertices[29] = centerY + Constants.RADIUS_CIRCLE
        vertices[30] = 0f
        vertices[31] = 1f
        vertices[32] = 0f
        vertices[33] = 1f //s
        vertices[34] = 0f //t
        //top left
        vertices[35] = centerX - Constants.RADIUS_CIRCLE
        vertices[36] = centerY + Constants.RADIUS_CIRCLE
        vertices[37] = 0f
        vertices[38] = 1f
        vertices[39] = 0f
        vertices[40] = 0f //s
        vertices[41] = 0f //t
    }

    override fun translateX(dX: Float) {
        centerX += dX
        initVertices()
    }

    override fun translateY(dY: Float) {
        centerY += dY
        initVertices()
    }
}