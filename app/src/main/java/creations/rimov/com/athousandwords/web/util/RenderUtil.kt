package creations.rimov.com.athousandwords.web.util

import android.opengl.GLES20
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer

object RenderUtil {

    fun createClientSideFloatBuff(nodeAttribs: FloatArray) = ByteBuffer
        .allocateDirect(nodeAttribs.size * Constants.BYTES_PER_FLOAT)
        .order(ByteOrder.nativeOrder()).asFloatBuffer()
        .apply {
            put(nodeAttribs)
        }

    /**
     * Create a VBO
     * @param clientSideBuffers: native-heap float buffers from ByteBuffer.allocateDirect(...)
     * @param drawMode: options are GL_STATIC_DRAW, GL_DYNAMIC_DRAW, and GL_STREAM_DRAW
     */
    fun createVbo(vboHandle: Int, clientSideBuffers: Array<FloatBuffer?>, drawMode: Int = GLES20.GL_STATIC_DRAW) {

        for(idx in clientSideBuffers.indices) {
            clientSideBuffers[idx]!!.position(0)
            GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, vboHandle)
            GLES20.glBufferData(
                GLES20.GL_ARRAY_BUFFER, clientSideBuffers[idx]!!.capacity() * Constants.BYTES_PER_FLOAT,
                clientSideBuffers[idx], drawMode)

            clientSideBuffers[idx]!!.limit(0)
            clientSideBuffers[idx] = null
        }

        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0)
    }
}