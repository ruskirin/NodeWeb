package creations.rimov.com.athousandwords_v1.web.util

import android.content.Context
import android.os.Environment
import android.util.Log
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

object CameraUtil {

    class ImageFile(val file: File, val storagePath: String)

    //TODO: handle IOException
    @JvmStatic
    fun createImageFile(context: Context): ImageFile {
        //TODO: consider modifying locale based on phone location
        val time = SimpleDateFormat("yyMMdd_HHmmss", Locale.US).format(Date())
        val storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val file = File.createTempFile("aThousandWords_Node_$time", ".jpg", storageDir)

        Log.i("ImageFile", "File path: ${file.absoluteFile}")

        return ImageFile(file, file.absolutePath)
    }
}