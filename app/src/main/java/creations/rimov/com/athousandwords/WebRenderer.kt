package creations.rimov.com.athousandwords

import android.content.Context
import android.opengl.GLES20
import android.opengl.GLSurfaceView
import creations.rimov.com.athousandwords.objects.Node
import creations.rimov.com.athousandwords.objects.Shapes
import creations.rimov.com.athousandwords.util.*
import creations.rimov.com.athousandwords.util.shaders.ShaderUtil
import java.nio.FloatBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class WebRenderer(private val context: Context) : GLSurfaceView.Renderer {
    object This {
        const val NODE_COORD_COMPONENTS = 2
        const val NODE_COLOR_COMPONENTS = 3
        const val NODE_TEX_COMPONENTS = 2
        const val NODE_TOTAL_COMPONENTS = NODE_COLOR_COMPONENTS + NODE_COORD_COMPONENTS + NODE_TEX_COMPONENTS
        const val NODE_STRIDE = NODE_TOTAL_COMPONENTS * Constants.BYTES_PER_FLOAT

        //4 corners + 1 repeat corner
        const val POINTS_PER_NODE = 5
    }

    //Used for node size
    private var magSize = 1f

    private val projectionMatrix = FloatArray(16)

    private val nodeStaticList = mutableListOf<Node>()

    private var nodeVertexArrayMove = FloatArray(0)
    private var nodeVertexArrayStatic = FloatArray(0)
    private var nodeVertexBuffMove: FloatBuffer? = null
    private var nodeVertexBuffStatic: FloatBuffer? = null

    private val nodeVboArray = IntArray(2)
    private val staticVbo = nodeVboArray[0]
    private val tempVbo = nodeVboArray[1]

    private var uProjectionMatHandle: Int = 0

    private var aNodeCenterHandle: Int = 0
    private var aNodeVertColorHandle: Int = 0
    private var aNodeTexCoordsHandle: Int = 0

    private var uNodeSamplerHandle: Int = 0
    private var uNodeTextureDataHandle: Int = 0


    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {

        val vertexShaders = intArrayOf(R.raw.node_vertex_shader)
        val fragShaders = intArrayOf(R.raw.node_frag_shader)
        val program = ShaderUtil.createProgram(context, vertexShaders, fragShaders)

        if (ShaderUtil.validProgram(program))
            GLES20.glUseProgram(program)

        uProjectionMatHandle = GLES20.glGetUniformLocation(program, ShaderUtil.NodeVertexConsts.U_PROJECTION_MAT)
        aNodeCenterHandle = GLES20.glGetAttribLocation(program, ShaderUtil.NodeVertexConsts.A_NODE_CENTER)
        aNodeVertColorHandle = GLES20.glGetAttribLocation(program, ShaderUtil.NodeVertexConsts.A_NODE_VERT_COLOR)
        aNodeTexCoordsHandle = GLES20.glGetAttribLocation(program, ShaderUtil.NodeVertexConsts.A_NODE_TEX_COORDS)
        uNodeSamplerHandle = GLES20.glGetUniformLocation(program, ShaderUtil.NodeFragConsts.U_NODE_SAMPLER)

        uNodeTextureDataHandle = TextureUtil.loadTexture(context, R.drawable.ic_picture)
    }

    override fun onDrawFrame(gl: GL10?) {

        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)

        //set value for projection matrix
        GLES20.glUniformMatrix4fv(uProjectionMatHandle, 1, false, projectionMatrix, 0)

        //bind bitmap
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0)
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, uNodeTextureDataHandle)
        GLES20.glUniform1i(uNodeSamplerHandle, 0) //Texture Unit 0

        /**
         * STATIC
         */
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, nodeVboArray[0])
        GLES20.glEnableVertexAttribArray(aNodeCenterHandle)
        GLES20.glVertexAttribPointer(aNodeCenterHandle,
            This.NODE_COORD_COMPONENTS, GLES20.GL_FLOAT, false,
            This.NODE_STRIDE, 0)

        GLES20.glEnableVertexAttribArray(aNodeVertColorHandle)
        GLES20.glVertexAttribPointer(aNodeVertColorHandle,
            This.NODE_COLOR_COMPONENTS, GLES20.GL_FLOAT, false,
            This.NODE_STRIDE, This.NODE_COORD_COMPONENTS * Constants.BYTES_PER_FLOAT)

        GLES20.glEnableVertexAttribArray(aNodeTexCoordsHandle)
        GLES20.glVertexAttribPointer(aNodeTexCoordsHandle,
            This.NODE_TEX_COMPONENTS, GLES20.GL_FLOAT, false,
            This.NODE_STRIDE, (This.NODE_COORD_COMPONENTS + This.NODE_COLOR_COMPONENTS) * Constants.BYTES_PER_FLOAT)

        if(nodeVertexArrayStatic.size > 0) {
            for (first in 0..(nodeVertexArrayStatic.size / This.NODE_TOTAL_COMPONENTS) step This.POINTS_PER_NODE) {
                GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, first, This.POINTS_PER_NODE)
            }
        }

        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0)


        /**
         * DYNAMIC
         */
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, nodeVboArray[1])
        GLES20.glEnableVertexAttribArray(aNodeCenterHandle)
        GLES20.glVertexAttribPointer(aNodeCenterHandle,
            This.NODE_COORD_COMPONENTS, GLES20.GL_FLOAT, false,
            This.NODE_STRIDE, 0)

        GLES20.glEnableVertexAttribArray(aNodeVertColorHandle)
        GLES20.glVertexAttribPointer(aNodeVertColorHandle,
            This.NODE_COLOR_COMPONENTS, GLES20.GL_FLOAT, false,
            This.NODE_STRIDE, This.NODE_COORD_COMPONENTS * Constants.BYTES_PER_FLOAT)

        GLES20.glEnableVertexAttribArray(aNodeTexCoordsHandle)
        GLES20.glVertexAttribPointer(aNodeTexCoordsHandle,
            This.NODE_TEX_COMPONENTS, GLES20.GL_FLOAT, false,
            This.NODE_STRIDE, (This.NODE_COORD_COMPONENTS + This.NODE_COLOR_COMPONENTS) * Constants.BYTES_PER_FLOAT)

        if(nodeVertexArrayMove.size > 0) {
            for (first in 0..(nodeVertexArrayMove.size / This.NODE_TOTAL_COMPONENTS) step This.POINTS_PER_NODE) {
                GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, first, This.POINTS_PER_NODE)
            }
        }

        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0)
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)

        GeneralUtil.createOrthoProjection(width, height, projectionMatrix)
    }

    /**
     * TODO: at the moment a new Node object is built every drag-tick, which is expensive
     *  provide a static method which just returns a vertex array based on a center coordinate
     */
    fun buildNodeDrag(center: Shapes.Point) {
        nodeVertexArrayMove = Node(center, magSize).getVertices()

        nodeVertexBuffMove = RenderUtil.createClientSideFloatBuff(nodeVertexArrayMove)

        if (tempVbo != 0) {
            //Clear old buffer, create new one
            val oldBuff = intArrayOf(nodeVboArray[1])
            GLES20.glDeleteBuffers(oldBuff.size, oldBuff, 0)
        }

        GLES20.glGenBuffers(1, nodeVboArray, 1)
        RenderUtil.createVbo(nodeVboArray[1], arrayOf(nodeVertexBuffMove), GLES20.GL_DYNAMIC_DRAW)
    }

    fun buildNodeStatic(center: Shapes.Point) {
        //Store a node object in a separate array
        nodeStaticList.add(Node(center, magSize))

        //Store vertex info for rendering
        nodeVertexArrayStatic = nodeVertexArrayStatic.plus(
            nodeStaticList.last().getVertices())

        nodeVertexBuffStatic = RenderUtil.createClientSideFloatBuff(nodeVertexArrayStatic)

        if (staticVbo != 0) {
            //Clear old buffer, create new one
            val oldBuff = intArrayOf(nodeVboArray[1])
            GLES20.glDeleteBuffers(oldBuff.size, oldBuff, 0)
        }

        GLES20.glGenBuffers(1, nodeVboArray, 0)
        RenderUtil.createVbo(nodeVboArray[0], arrayOf(nodeVertexBuffStatic), GLES20.GL_STATIC_DRAW)
    }
}