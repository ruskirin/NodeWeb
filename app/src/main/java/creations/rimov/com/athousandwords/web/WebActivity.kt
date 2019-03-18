package creations.rimov.com.athousandwords.web

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.opengl.GLSurfaceView
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.content.FileProvider
import com.google.android.material.floatingactionbutton.FloatingActionButton
import creations.rimov.com.athousandwords.R
import creations.rimov.com.athousandwords.web.objects.Shapes
import creations.rimov.com.athousandwords.web.util.CameraUtil
import creations.rimov.com.athousandwords.web.util.Constants
import java.io.File
import java.io.IOException
import kotlin.math.abs

class WebActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var webView: GLSurfaceView
    private val webRenderer = WebRenderer(this)

    private lateinit var nodeControlFAButton: FloatingActionButton
    private lateinit var nodeControlPanel: LinearLayout
    private lateinit var nodeControlCamera: ImageButton
    private lateinit var nodeControlPhotos: ImageButton
    private lateinit var nodeControlFile: ImageButton

    //Flags for type of gesture performed
    private var nodeTouched = false
    private var nodePressed = false
    private var regPressed = false

    //Path for newly captured photo; works in tandem with "nodePhotoCaptured" flag
    private var photoPath = ""
    private var nodePhotoCaptured = false

    object This {
        const val TAKE_PICTURE = 1 //Used in StartActivityForResult
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.web_layout)

        webView = findViewById(R.id.web_layout_glsurfaceview)

        nodeControlFAButton = findViewById(R.id.web_layout_fabutton)
        nodeControlPanel = findViewById(R.id.web_layout_controlpanel)
        nodeControlCamera = findViewById(R.id.web_layout_control_camera)
        nodeControlPhotos = findViewById(R.id.web_layout_control_photos)
        nodeControlFile = findViewById(R.id.web_layout_control_file)

        val gestureDetector = GestureDetector(this, UiGestureListener())
        gestureDetector.setIsLongpressEnabled(true)

        /**Check for appropriate OpenGL version (> OpenGL 2.0)**/
        val configInfo = (getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager)
            .deviceConfigurationInfo

        if (configInfo.reqGlEsVersion >= 0x20000) {
            webView.setEGLContextClientVersion(2)
            webView.setRenderer(webRenderer)
            webView.renderMode = GLSurfaceView.RENDERMODE_WHEN_DIRTY

        } else
            Toast.makeText(this, "Wrong OpenGL ES version.", Toast.LENGTH_SHORT).show()
        /****/

        webView.setOnTouchListener {view, event ->
            if(event != null) {
                //Run by gestureDetector to handle extra functionality
                gestureDetector.onTouchEvent(event)

                //Node is now only created after taking a picture, the photo path is saved inside Node object
                //TODO: while ACTION_MOVE, node should be textured as a dashed outline
                //       on ACTION_UP, the image is processed as a texture and assigned to a texture unit, which is then
                //       shrunk (or mipmapped) and used as the thumbnail for the node
                //TODO: then on node click, a new screen opens showing the full captured image

                when(event.action) {

                    MotionEvent.ACTION_DOWN -> {
                        if(nodePhotoCaptured) {

                            webView.queueEvent {
                                webRenderer.buildNodeDrag(Shapes.Point(event.x, event.y))
                            }
                            webView.requestRender()
                        }
                    }

                    MotionEvent.ACTION_MOVE -> {
                        if(nodePhotoCaptured) {

                            webView.queueEvent {
                                webRenderer.buildNodeDrag(Shapes.Point(event.x, event.y))
                            }
                            webView.requestRender()
                        }
                    }

                    MotionEvent.ACTION_UP -> {
                        if(nodePhotoCaptured) {

                            webView.queueEvent {
                                webRenderer.buildNodeStatic(Shapes.Point(event.x, event.y), photoPath)
                                webRenderer.clearNodeDrag()

                                //Reset flag and temp holder
                                nodePhotoCaptured = false
                                photoPath = ""
                            }

                            webView.requestRender()
                        }
                    }
                }
                true

            } else false
        }

        nodeControlFAButton.setOnClickListener(this)
        nodeControlCamera.setOnClickListener(this)
        nodeControlPhotos.setOnClickListener(this)
        nodeControlFile.setOnClickListener(this)
    }

    override fun onPause() {
        super.onPause()
        //maybe set up a check for a set renderer
        webView.onPause()
    }

    override fun onResume() {
        super.onResume()
        //maybe set up a check for a set renderer
        webView.onResume()
    }

    override fun onClick(v: View?) {

        if(v != null) {

            when (v.id) {
                R.id.web_layout_fabutton -> {
                    if(nodeControlPanel.visibility == View.GONE)
                        nodeControlPanel.visibility = View.VISIBLE
                    else
                        nodeControlPanel.visibility = View.GONE
                }
                /**
                 * For further reference: https://developer.android.com/training/camera/photobasics.html#kotlin
                 */
                R.id.web_layout_control_camera -> {
                    //TODO: save file path in CameraUtil.createImageFile, then store that in Node. Render texture from there
                    val nodeCameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    //Ensures that an app exists that can handle the intent
                    nodeCameraIntent.resolveActivity(packageManager)

                    //TODO: handle errors with file creation
                    val imageFile: CameraUtil.ImageFile? = try {
                        CameraUtil.createImageFile(this)

                    } catch(e: IOException) {
                        e.printStackTrace()
                        null
                    }

                    if(imageFile != null) {
                        val imageUri = FileProvider.getUriForFile(this,
                            "com.rimov.creations.athousandwords.imageprovider",
                            imageFile.file)
                        nodeCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
                        startActivityForResult(nodeCameraIntent, This.TAKE_PICTURE)

                        nodePhotoCaptured = true
                        photoPath = imageFile.storagePath
                    }
                }
                /**
                 * TODO: Follow bookmarked tutorial for working with Gallery
                 *        Goal is to open a view displaying photos in phone storage
                 */
                R.id.web_layout_control_photos -> {

                }
                /**
                 * TODO: Follow bookmarked tutorial for working with Storage
                 *        Goal is to open a directory displaying stores files (consider perhaps only eligible formats, cutting down work)
                 */
                R.id.web_layout_control_file -> {

                }
            }
        }
    }

    inner class UiGestureListener : GestureDetector.SimpleOnGestureListener() {

        override fun onDown(event: MotionEvent): Boolean {

            if(nodeControlPanel.visibility == View.VISIBLE)
                nodeControlPanel.visibility = View.GONE

            //TODO: consider returning node ID
            for(node in webRenderer.totalNodeSet) {
                if(abs(event.x - node.centerX) <= (WebRenderer.This.NODE_WIDTH / 2)
                    && abs(event.y - node.centerY) <= (WebRenderer.This.NODE_LENGTH / 2)) {

                    nodeTouched = true
                    break
                }
            }

            return super.onDown(event)
        }

        override fun onLongPress(event: MotionEvent?) {
            /*
            if(nodeTouched) {
                nodePressed = true

            } else
                regPressed = true
            */
        }
    }
}
