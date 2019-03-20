package creations.rimov.com.athousandwords_v1.web.objects

import creations.rimov.com.athousandwords_v1.web.WebRenderer
import java.text.SimpleDateFormat
import java.util.*


class Node(center: Shapes.Point) : Shapes.Base(center) {
    val id: String = SimpleDateFormat("yyMMddHHmmss", Locale.US).format(Date())

    //Texture file path
    var texturePath: String = ""
    //Bound texture unit number
    var textureUnit: Int = -1

    //Is node being used?
    var isActive = false

    //6 * 7
    private val vertices = FloatArray(WebRenderer.This.POINTS_PER_NODE * WebRenderer.This.NODE_TOTAL_COMPONENTS)

    init {
        initVertices()
    }

    fun getVertices() = vertices

    private fun initVertices() {
        //center
        vertices[0] = centerX
        vertices[1] = centerY
        vertices[2] = 1f
        vertices[3] = 1f
        vertices[4] = 1f
        vertices[5] = 0.5f //s
        vertices[6] = 0.5f //t
        //top left
        vertices[7] = centerX - (WebRenderer.This.NODE_WIDTH / 2)
        vertices[8] = centerY + (WebRenderer.This.NODE_LENGTH / 2)
        vertices[9] = 1f
        vertices[10] = 1f
        vertices[11] = 1f
        vertices[12] = 1f //s
        vertices[13] = 0f //t
        //bottom left
        vertices[14] = centerX - (WebRenderer.This.NODE_WIDTH / 2)
        vertices[15] = centerY - (WebRenderer.This.NODE_LENGTH / 2)
        vertices[16] = 1f
        vertices[17] = 1f
        vertices[18] = 1f
        vertices[19] = 1f //s
        vertices[20] = 1f //t
        //bottom right
        vertices[21] = centerX + (WebRenderer.This.NODE_WIDTH / 2)
        vertices[22] = centerY - (WebRenderer.This.NODE_LENGTH / 2)
        vertices[23] = 1f
        vertices[24] = 1f
        vertices[25] = 1f
        vertices[26] = 0f //s
        vertices[27] = 1f //t
        //top right
        vertices[28] = centerX + (WebRenderer.This.NODE_WIDTH / 2)
        vertices[29] = centerY + (WebRenderer.This.NODE_LENGTH / 2)
        vertices[30] = 1f
        vertices[31] = 1f
        vertices[32] = 1f
        vertices[33] = 0f //s
        vertices[34] = 0f //t
        //top left
        vertices[35] = centerX - (WebRenderer.This.NODE_WIDTH / 2)
        vertices[36] = centerY + (WebRenderer.This.NODE_LENGTH / 2)
        vertices[37] = 1f
        vertices[38] = 1f
        vertices[39] = 1f
        vertices[40] = 1f //s
        vertices[41] = 0f //t
    }

    /** Check if node is within a certain distance from a specified point
     * @param from: starting measure point
     * @param distanceX, distanceY: range from 'from'-point to check for the node
     */
    fun inRange(from: Shapes.Point, distanceX: Int, distanceY: Int): Boolean {
        val topL = Shapes.Point(vertices[7], vertices[8])
        val topR = Shapes.Point(vertices[28], vertices[29])
        val botL = Shapes.Point(vertices[14], vertices[15])
        val botR = Shapes.Point(vertices[21], vertices[22])

        return ((topL.x - from.x <= distanceX) && (topL.y - from.y <= distanceY))
                || ((topR.x - from.x <= distanceX) && (topR.y - from.y <= distanceY))
                || ((botL.x - from.x <= distanceX) && (botL.y - from.y <= distanceY))
                || ((botR.x - from.x <= distanceX) && (botR.y - from.y <= distanceY))
    }

    //Node is being used
    fun activate(textureUnit: Int) {
        isActive = true
        this.textureUnit = textureUnit
    }
    //Node no longer used
    fun deactivate() {
        isActive = false
        textureUnit = -1
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