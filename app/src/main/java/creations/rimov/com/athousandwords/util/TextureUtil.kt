package creations.rimov.com.athousandwords.util

import android.content.Context
import android.graphics.BitmapFactory
import android.opengl.GLES20
import android.opengl.GLUtils
import android.util.Log

object TextureUtil {

    fun loadTexture(context: Context, resId: Int): Int {
        val textureObjHandles = IntArray(1)
        GLES20.glGenTextures(1, textureObjHandles, 0)

        val options = BitmapFactory.Options()
        options.inScaled = false

        //if bitmap == null, resource ID could not be decoded
        val bitmap = BitmapFactory.decodeResource(context.resources, resId, options)
        if(bitmap == null) {
            Log.e("TextureUtil", "Could not decode resource, deleting texture handles")
            GLES20.glDeleteTextures(1, textureObjHandles, 0)
            return 0
        }

        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureObjHandles[0])
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR_MIPMAP_LINEAR)
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR)

        //load bitmap into OpenGL
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0)

        //bitmap loaded in, can release memory
        bitmap.recycle()

        GLES20.glGenerateMipmap(GLES20.GL_TEXTURE_2D)
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0)

        return textureObjHandles[0]
    }
}