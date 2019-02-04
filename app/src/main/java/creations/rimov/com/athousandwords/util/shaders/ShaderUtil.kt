package creations.rimov.com.athousandwords.util.shaders

import android.content.Context
import android.content.res.Resources
import android.opengl.GLES20
import android.util.Log
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

object ShaderUtil {

    private val TAG = javaClass.simpleName

    object NodeVertexConsts {
        const val A_NODE_VERT_COLOR = "a_NodeVertexColor"
        const val A_NODE_CENTER = "a_NodeCenter"
        const val A_NODE_TEX_COORDS = "a_NodeTexCoords"

        const val U_PROJECTION_MAT = "u_ProjectionMat"

        val texCoords = floatArrayOf(
            0.5f, 0.5f,
            0f, 0f,
            0f, 1f,
            1f, 1f,
            1f, 0f,
            0f, 0f
        )
    }

    object NodeFragConsts {
        const val U_NODE_SAMPLER = "u_NodeSampler"
    }

    /**
     * Create a program from passed shaders
     */
    @JvmStatic
    fun createProgram(context: Context, vertexShaders: IntArray, fragShaders: IntArray): Int {
        val shaderObjects = IntArray(vertexShaders.size + fragShaders.size)

        var current = 0
        for(shader in vertexShaders) {
            shaderObjects.set(current, compileShader(GLES20.GL_VERTEX_SHADER, readShaderSource(context, shader)))
            current++
        }

        for(shader in fragShaders) {
            shaderObjects.set(current, compileShader(GLES20.GL_FRAGMENT_SHADER, readShaderSource(context, shader)))
            current++
        }

        return linkProgram(shaderObjects)
    }

    @JvmStatic
    private fun readShaderSource(context: Context, resource: Int): String {

        val stringBuilder = StringBuilder()

        try {
            val inputStreamReader = InputStreamReader(context.resources.openRawResource(resource))
            val buffReader = BufferedReader(inputStreamReader)

            for (line in buffReader.readLines()) {
                stringBuilder.append(line) ?: break
                stringBuilder.append("\n")
            }

        } catch (e: IOException) {
            RuntimeException("Error reading shader")

        } catch (e: Resources.NotFoundException) {
            RuntimeException("Could not find shader at location")
        }

        return stringBuilder.toString()
    }

    @JvmStatic
    private fun compileShader(type: Int, shaderSource: String): Int {
        val shaderObj = GLES20.glCreateShader(type)

        if (shaderObj == 0) {
            Log.e(TAG,"Shader not created; illegal shader type")
            return 0
        }

        GLES20.glShaderSource(shaderObj, shaderSource)
        GLES20.glCompileShader(shaderObj)

        val compileStatus = IntArray(1)
        GLES20.glGetShaderiv(shaderObj, GLES20.GL_COMPILE_STATUS, compileStatus, 0)

        if (compileStatus[0] == 0) {
            Log.i(TAG, "Bad compilation of shader ($shaderObj): ${GLES20.glGetShaderInfoLog(shaderObj)}")
            GLES20.glDeleteShader(shaderObj)
            return 0
        }

        return shaderObj
    }

    @JvmStatic
    private fun linkProgram(shaderObjects: IntArray): Int {
        val program = GLES20.glCreateProgram()

        if (program == 0) {
            Log.e(TAG, "Program was not created!")
            return program
        }
        //shaders were verified prior, not checking for errors
        for(shader in shaderObjects) {
            GLES20.glAttachShader(program, shader)
        }
        GLES20.glLinkProgram(program)

        val linkStatus = IntArray(1)
        GLES20.glGetProgramiv(program, GLES20.GL_LINK_STATUS, linkStatus, 0)

        if(linkStatus[0] == 0) {
            Log.i(TAG, "Bad link of program ($program): ${GLES20.glGetProgramInfoLog(program)}")
            return 0
        }

        return program
    }

    @JvmStatic
    fun validProgram(program: Int): Boolean {
        val validStatus = IntArray(1)

        GLES20.glValidateProgram(program)
        GLES20.glGetProgramiv(program, GLES20.GL_VALIDATE_STATUS, validStatus, 0)
        Log.i(TAG, "Results of program ($program) validation: ${GLES20.glGetProgramInfoLog(program)}")

        return validStatus[0] != 0
    }
}