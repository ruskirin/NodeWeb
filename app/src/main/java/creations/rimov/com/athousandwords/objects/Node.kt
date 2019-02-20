package creations.rimov.com.athousandwords.objects

import android.util.Log
import creations.rimov.com.athousandwords.WebRenderer
import creations.rimov.com.athousandwords.util.Constants


class Node(center: Shapes.Point) : Shapes.Base(center) {
    private val id: Int = (0..1000000).random()
    //6 * 7
    //private val vertices = FloatArray(WebRenderer.This.POINTS_PER_NODE * WebRenderer.This.NODE_TOTAL_COMPONENTS)
    private val vertices = FloatArray(WebRenderer.This.POINTS_PER_FULL_NODE * WebRenderer.This.NODE_TOTAL_COMPONENTS)

    //Distance from center of node; for splitting node halves for summary box
    private var separation: Float = 30f

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

    fun getId() = id
    fun getVertices() = vertices

    /**
     * TODO: issue is the disapperance of top half of the nodes; possible workaround is rendering as triangles instead of fan
     */

    //Split node into 2 parts; drawn using strips with degenerate triangle transition
    private fun initVertices() {
        //HALF 1
        //center right
        vertices[0] = centerX + Constants.RADIUS_CIRCLE
        vertices[1] = centerY + separation
        vertices[2] = 0f
        vertices[3] = 1f
        vertices[4] = 0f
        vertices[5] = 1f   //s
        vertices[6] = 0.5f //t
        //top right
        vertices[7] = centerX + Constants.RADIUS_CIRCLE
        vertices[8] = centerY + Constants.RADIUS_CIRCLE + separation
        vertices[9] = 0f
        vertices[10] = 1f
        vertices[11] = 0f
        vertices[12] = 1f //s
        vertices[13] = 0f //t
        //top left
        vertices[14] = centerX - Constants.RADIUS_CIRCLE
        vertices[15] = centerY + Constants.RADIUS_CIRCLE + separation
        vertices[16] = 0f
        vertices[17] = 1f
        vertices[18] = 0f
        vertices[19] = 0f //s
        vertices[20] = 0f //t
        //center left
        vertices[21] = centerX - Constants.RADIUS_CIRCLE
        vertices[22] = centerY + separation
        vertices[23] = 0f
        vertices[24] = 1f
        vertices[25] = 0f
        vertices[26] = 0f //s
        vertices[27] = 0.5f //t

        //HALF 2
        //center left
        vertices[28] = centerX - Constants.RADIUS_CIRCLE
        vertices[29] = centerY - separation
        vertices[30] = 0f
        vertices[31] = 1f
        vertices[32] = 0f
        vertices[33] = 0f //s
        vertices[34] = 0.5f //t
        //bottom left
        vertices[35] = centerX - Constants.RADIUS_CIRCLE
        vertices[36] = centerY - Constants.RADIUS_CIRCLE - separation
        vertices[37] = 0f
        vertices[38] = 1f
        vertices[39] = 0f
        vertices[40] = 0f //s
        vertices[41] = 1f //t
        //bottom right
        vertices[42] = centerX + Constants.RADIUS_CIRCLE
        vertices[43] = centerY - Constants.RADIUS_CIRCLE - separation
        vertices[44] = 0f
        vertices[45] = 1f
        vertices[46] = 0f
        vertices[47] = 1f //s
        vertices[48] = 1f //t
        //center right
        vertices[49] = centerX + Constants.RADIUS_CIRCLE
        vertices[50] = centerY - separation
        vertices[51] = 0f
        vertices[52] = 1f
        vertices[53] = 0f
        vertices[54] = 1f   //s
        vertices[55] = 0.5f //t
    }

    /*
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
    */

    override fun translateX(dX: Float) {
        centerX += dX
        initVertices()
    }

    override fun translateY(dY: Float) {
        centerY += dY
        initVertices()
    }
}