package creations.rimov.com.athousandwords.web.util

import android.opengl.GLES20
import creations.rimov.com.athousandwords.web.WebRenderer
import creations.rimov.com.athousandwords.web.objects.Node
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer

object RenderUtil {

    fun setVertexAttribPointer(vertexBuffer: FloatBuffer?,
                               positionHandle: Int, positionComp: Int,
                               colorHandle: Int?, colorComp: Int?,
                               textureHandle: Int?, textureComp: Int?) {

        val stride = (positionComp + (colorComp ?: 0) + (textureComp ?: 0)) * Constants.BYTES_PER_FLOAT

        if(vertexBuffer != null) {
            vertexBuffer.position(0)

            GLES20.glVertexAttribPointer(
                positionHandle,
                positionComp, GLES20.GL_FLOAT, false,
                stride, vertexBuffer
            )
            GLES20.glEnableVertexAttribArray(positionHandle)

            if (colorHandle != null && colorComp != null) {
                vertexBuffer.position(positionComp)

                GLES20.glVertexAttribPointer(
                    colorHandle,
                    colorComp, GLES20.GL_FLOAT, false,
                    stride, vertexBuffer
                )
                GLES20.glEnableVertexAttribArray(colorHandle)
            }

            if (textureHandle != null && textureComp != null) {
                vertexBuffer.position(positionComp + (colorComp ?: 0))

                GLES20.glVertexAttribPointer(
                    textureHandle,
                    textureComp, GLES20.GL_FLOAT, false,
                    stride, vertexBuffer
                )
                GLES20.glEnableVertexAttribArray(textureHandle)
            }

        } else {

            GLES20.glVertexAttribPointer(
                positionHandle,
                positionComp, GLES20.GL_FLOAT, false,
                stride, 0
            )
            GLES20.glEnableVertexAttribArray(positionHandle)

            if (colorHandle != null && colorComp != null) {
                GLES20.glVertexAttribPointer(
                    colorHandle,
                    colorComp, GLES20.GL_FLOAT, false,
                    stride, positionComp * Constants.BYTES_PER_FLOAT
                )
                GLES20.glEnableVertexAttribArray(colorHandle)
            }

            if (textureHandle != null && textureComp != null) {
                GLES20.glVertexAttribPointer(
                    textureHandle,
                    textureComp, GLES20.GL_FLOAT, false,
                    stride, (positionComp + (colorComp ?: 0)) * Constants.BYTES_PER_FLOAT
                )
                GLES20.glEnableVertexAttribArray(textureHandle)
            }
        }
    }

    fun createClientSideFloatBuff(nodeList: List<Node>): FloatBuffer {

        //Assuming every elements of the nodeList has equal vertex count
        val capacity = nodeList.size * nodeList.get(0).getVertices().size * Constants.BYTES_PER_FLOAT

        return ByteBuffer.allocateDirect(capacity)
            .order(ByteOrder.nativeOrder()).asFloatBuffer()
            .apply {
                for (node in nodeList) {
                    put(node.getVertices())
                }
            }
    }

    /**
     * Create a VBO
     * @param clientSideBuffers: native-heap float buffers from ByteBuffer.allocateDirect(...)
     * @param drawMode: options are GL_STATIC_DRAW, GL_DYNAMIC_DRAW, and GL_STREAM_DRAW
     */
    fun createVbo(vboHandle: Int, clientSideBuffers: Array<FloatBuffer?>, drawMode: Int = GLES20.GL_STATIC_DRAW) {

        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, vboHandle)

        for(idx in clientSideBuffers.indices) {
            clientSideBuffers[idx]!!.position(0)
            GLES20.glBufferData(
                GLES20.GL_ARRAY_BUFFER, clientSideBuffers[idx]!!.capacity() * Constants.BYTES_PER_FLOAT,
                clientSideBuffers[idx], drawMode)

            clientSideBuffers[idx]!!.limit(0)
            clientSideBuffers[idx] = null
        }

        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0)
    }
}