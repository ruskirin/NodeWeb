package creations.rimov.com.athousandwords.web.util

import android.content.Context
import android.graphics.BitmapFactory
import android.opengl.GLES20
import android.opengl.GLUtils
import android.util.Log

object TextureUtil {

    fun loadTexture(context: Context, resArray: IntArray, createMipmap: Boolean): IntArray {
        val textureObjHandles = IntArray(resArray.size)
        GLES20.glGenTextures(resArray.size, textureObjHandles, 0)

        val options = BitmapFactory.Options()
        options.inScaled = false

        for(idx in resArray.indices) {
            //if bitmap == null, resource ID could not be decoded
            val bitmap = BitmapFactory.decodeResource(context.resources, resArray[idx], options)
            if (bitmap == null) {
                //TODO: address fail in decoding resources
                Log.e("TextureUtil", "Could not decode resource ${resArray[idx]}")
            }

            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureObjHandles[idx])
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR_MIPMAP_LINEAR)
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR)

            //load bitmap into OpenGL
            GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0)

            //bitmap loaded in, can release memory
            bitmap.recycle()

            if(createMipmap)
                GLES20.glGenerateMipmap(GLES20.GL_TEXTURE_2D)

            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0)
        }

        return textureObjHandles
    }
}