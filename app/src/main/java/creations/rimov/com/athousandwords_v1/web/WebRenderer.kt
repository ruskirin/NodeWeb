package creations.rimov.com.athousandwords_v1.web

import android.content.Context
import android.opengl.GLES20
import android.opengl.GLSurfaceView
import creations.rimov.com.athousandwords_v1.R
import creations.rimov.com.athousandwords_v1.web.objects.Node
import creations.rimov.com.athousandwords_v1.web.objects.Shapes
import creations.rimov.com.athousandwords_v1.web.util.Constants
import creations.rimov.com.athousandwords_v1.web.util.MatrixUtil
import creations.rimov.com.athousandwords_v1.web.util.RenderUtil
import creations.rimov.com.athousandwords_v1.web.util.TextureUtil
import creations.rimov.com.athousandwords_v1.web.util.shaders.ShaderUtil
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

        //1 center + 4 corners + 1 repeat corner to tie together shape
        const val POINTS_PER_NODE = 6

        const val NODE_LENGTH = 300f
        const val NODE_WIDTH = 200f

        const val NODE_TEXTURE_AUDIO = R.drawable.ic_web_audio
        const val NODE_TEXTURE_TEXT = R.drawable.ic_web_text
        const val NODE_TEXTURE_VIDEO = R.drawable.ic_web_video
        const val NODE_TEXTURE_IMAGE = R.drawable.ic_web_image
        const val NODE_TEXTURE_EMPTY = R.drawable.ic_web_emptynode
        const val NODE_TEXTURE_OUTLINE = R.drawable.ic_node_outline

        @JvmStatic
        val NODE_TEXTURE_RES_ARRAY = intArrayOf(
            NODE_TEXTURE_AUDIO,
            NODE_TEXTURE_VIDEO,
            NODE_TEXTURE_TEXT,
            NODE_TEXTURE_IMAGE,
            NODE_TEXTURE_EMPTY,
            NODE_TEXTURE_OUTLINE
        )
    }

    //If more required must use another fragment shader
    private val maxFragmentTextureUnits = IntArray(1)
    private val maxTotalTextureUnits = IntArray(1)

    //Store all created nodes
    val totalNodeSet = mutableSetOf<Node>()
    //Store rendered nodes
    val activeNodeList = mutableListOf<Node>()
    //Keep track of active texture units (size set in onSurfaceCreated);
    //Each unit is (index of the array) + GLES20.GL_TEXTURE0
    private var activeTextureUnits = BooleanArray(0)
    private var tempNodeResTextureHandles = IntArray(0)

    //Screen size
    var screenW: Int = 0
    var screenH: Int = 0

    //Initial center point around which the web is built
    //var worldCenterX: Int = 0
    //var worldCenterY: Int = 0

    //Center point of the screen in the world
    var eyeX: Float = 0f
    var eyeY: Float = 0f

    //Register any movement
    var screenChanged = false

    private val projectionMatrix = FloatArray(16)

    private var nodeVertexBuffMove: FloatBuffer? = null
    private var nodeVertexBuffStatic: FloatBuffer? = null

    private val nodeVboArray = IntArray(1)
    private val staticVbo = nodeVboArray[0]

    private var uProjectionMatHandle: Int = 0

    private var aNodeVertPositionHandle: Int = 0
    private var aNodeVertColorHandle: Int = 0
    private var aNodeTexCoordsHandle: Int = 0

    private var uNodeSamplerHandle: Int = 0


    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {

        val vertexShaders = intArrayOf(R.raw.node_vertex_shader)
        val fragShaders = intArrayOf(R.raw.node_frag_shader)
        val program = ShaderUtil.createProgram(context, vertexShaders, fragShaders)

        //TODO: if program is not valid, should react
        if (ShaderUtil.validProgram(program))
            GLES20.glUseProgram(program)

        //Get maximum allowed texture units per fragment shader
        GLES20.glGetIntegerv(GLES20.GL_MAX_TEXTURE_IMAGE_UNITS, maxFragmentTextureUnits, 0)
        //Maximum allowed texture units per fragment + vertex shaders
        GLES20.glGetIntegerv(GLES20.GL_MAX_COMBINED_TEXTURE_IMAGE_UNITS, maxTotalTextureUnits, 0)

        //Trigger for loading node textures
        screenChanged = true
        //Context lost, reload all textures
        resetTextureUnits()

        uProjectionMatHandle = GLES20.glGetUniformLocation(program, ShaderUtil.NodeVertexConsts.U_PROJECTION_MAT)

        aNodeVertPositionHandle = GLES20.glGetAttribLocation(program, ShaderUtil.NodeVertexConsts.A_NODE_VERT_POS)
        aNodeVertColorHandle = GLES20.glGetAttribLocation(program, ShaderUtil.NodeVertexConsts.A_NODE_VERT_COLOR)
        aNodeTexCoordsHandle = GLES20.glGetAttribLocation(program, ShaderUtil.NodeVertexConsts.A_NODE_TEX_COORDS)

        uNodeSamplerHandle = GLES20.glGetUniformLocation(program, ShaderUtil.NodeFragConsts.U_NODE_SAMPLER)

        tempNodeResTextureHandles = TextureUtil.loadResourceTextures(context, This.NODE_TEXTURE_RES_ARRAY, false)

        for(idx in tempNodeResTextureHandles.indices) {
            GLES20.glActiveTexture(GLES20.GL_TEXTURE0 + idx)
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, tempNodeResTextureHandles[idx])
            activeTextureUnits[idx] = true
        }

        //Allows for texture transparency
        GLES20.glEnable(GLES20.GL_BLEND)
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA)
    }

    override fun onDrawFrame(gl: GL10?) {

        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)
        GLES20.glClearColor(0.1f, 0.3f, 0.5f, 0.1f)

        if(screenChanged)
            redrawScreen()


        if(nodeVertexBuffMove != null && nodeVertexBuffMove!!.capacity() > 0) {

            GLES20.glUniform1i(uNodeSamplerHandle, 5) //Texture Unit x
            GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, This.POINTS_PER_NODE)
        }
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {

        GLES20.glViewport(0, 0, width, height)

        if(screenW != width || screenH != height) {
            screenW = width
            screenH = height
            eyeX = screenW / 2f
            eyeY = screenH / 2f

            MatrixUtil.orthoTransform(width, height, projectionMatrix)
            //MatrixUtil.scaleTransform(1.0f, 1.0f, 1.0f, projectionMatrix)
        }
        //set value for projection matrix
        GLES20.glUniformMatrix4fv(uProjectionMatHandle, 1, false, projectionMatrix, 0)
    }

    fun buildNodeDrag(center: Shapes.Point) {

        clearNodeDrag()

        nodeVertexBuffMove = RenderUtil.createClientSideFloatBuff(listOf(Node(center)))

        RenderUtil.setVertexAttribPointer(nodeVertexBuffMove,
            aNodeVertPositionHandle, This.NODE_COORD_COMPONENTS,
            aNodeVertColorHandle, This.NODE_COLOR_COMPONENTS,
            aNodeTexCoordsHandle, This.NODE_TEX_COMPONENTS)
    }

    fun clearNodeDrag() {
        if(nodeVertexBuffMove != null) {
            nodeVertexBuffMove!!.limit(0)
            nodeVertexBuffMove = null
        }
    }

    fun buildNodeStatic(center: Shapes.Point, imagePath: String?) {

        val node = Node(center)
        node.texturePath = imagePath ?: ""

        //Store a node object in a separate array
        totalNodeSet.add(node)

        if(node.inRange(Shapes.Point(eyeX, eyeY), screenW / 2, screenH / 2)) {
            //Flag to recycle texture units for new nodes
            screenChanged = true
        }
    }

    //Called when OpenGL Context lost (check when onSurfaceCreated is called)
    private fun resetTextureUnits() {

        activeTextureUnits = BooleanArray(maxFragmentTextureUnits[0]) {false}

        activeNodeList.forEach {
            it.isActive = false
        }

        activeNodeList.clear()
    }

    private fun redrawScreen() {

        setActiveNodeList()

        if(activeNodeList.size > 0) {
            nodeVertexBuffStatic = RenderUtil.createClientSideFloatBuff(activeNodeList)

            if (staticVbo != 0) {
                //Clear old buffer, create new one
                GLES20.glDeleteBuffers(1, nodeVboArray, 0)
            }

            GLES20.glGenBuffers(1, nodeVboArray, 0)
            RenderUtil.createVbo(nodeVboArray[0], arrayOf(nodeVertexBuffStatic), GLES20.GL_STATIC_DRAW)

            setActiveTextures()

            GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, nodeVboArray[0])

            RenderUtil.setVertexAttribPointer(null,
                aNodeVertPositionHandle, This.NODE_COORD_COMPONENTS,
                aNodeVertColorHandle, This.NODE_COLOR_COMPONENTS,
                aNodeTexCoordsHandle, This.NODE_TEX_COMPONENTS)

            var initialVertex = 0
            for(node in activeNodeList) {
                //Set the node's texture unit from here after it has been uploaded to OpenGL
                GLES20.glUniform1i(uNodeSamplerHandle, node.textureUnit)
                GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, initialVertex, This.POINTS_PER_NODE)
                initialVertex += This.POINTS_PER_NODE
            }

            GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0)
        }

        screenChanged = false
    }

    private fun setActiveNodeList() {
        val radiusWidth = screenW / 2
        val radiusHeight = screenH / 2

        //Remove any inactive nodes outside the screen space
        activeNodeList.forEach {
            if(!it.inRange(Shapes.Point(eyeX, eyeY), radiusWidth, radiusHeight)) {
                //Texture no longer used, free up texture unit
                activeTextureUnits[it.textureUnit] = false
                activeNodeList.remove(it)
                it.deactivate()
            }
        }

        //Check for any new nodes within screen space
        totalNodeSet.forEach {
            if (it.inRange(Shapes.Point(eyeX, eyeY), radiusWidth, radiusHeight)
                && !it.isActive) {

                activeNodeList.add(it)
            }
        }
    }

    private fun setActiveTextures() {

        val paths = mutableListOf<String>() //Not yet loaded textures
        val nodeIndices = mutableListOf<Int>() //Corresponding textures' indices in activeNodeList
        //Get the texture paths from recently added nodes
        activeNodeList.forEachIndexed {idx, node ->
            if(!node.isActive) {
                nodeIndices.add(idx)
                paths.add(node.texturePath)
            }
        }

        //Bind textures to Texture Units and store the respective texture unit number in Node
        val handles = TextureUtil.loadNodeFileTextures(paths, true)
        handles.forEachIndexed {idx, handle ->
            val emptyUnit = activeTextureUnits.indexOfFirst {!it}

            GLES20.glActiveTexture(GLES20.GL_TEXTURE0 + emptyUnit)
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, handle)

            activeNodeList[nodeIndices[idx]].activate(emptyUnit)
            activeTextureUnits[emptyUnit] = true
        }
    }
}