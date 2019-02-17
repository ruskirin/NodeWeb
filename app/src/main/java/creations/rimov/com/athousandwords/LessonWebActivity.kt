package creations.rimov.com.athousandwords

import android.annotation.SuppressLint
import android.app.ActivityManager
import android.content.Context
import android.opengl.GLSurfaceView
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.widget.Toast
import creations.rimov.com.athousandwords.objects.Shapes
import creations.rimov.com.athousandwords.util.Constants
import creations.rimov.com.athousandwords.util.GeneralUtil
import kotlin.math.abs

class LessonWebActivity : AppCompatActivity() {

    private lateinit var webView: GLSurfaceView
    private val webRenderer = WebRenderer(this)

    //Flags for type of gesture performed
    private var nodeTouched = false
    private var nodePressed = false
    private var regPressed = false

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        webView = GLSurfaceView(this)

        val gestureDetector = GestureDetector(this, UiGestureListener())
        gestureDetector.setIsLongpressEnabled(true)

        val configInfo = (getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager)
            .deviceConfigurationInfo

        if (configInfo.reqGlEsVersion >= 0x20000) {
            webView.setEGLContextClientVersion(2)
            webView.setRenderer(webRenderer)
            webView.renderMode = GLSurfaceView.RENDERMODE_WHEN_DIRTY

        } else
            Toast.makeText(this, "Wrong OpenGL ES version.", Toast.LENGTH_SHORT).show()

        setContentView(webView)

        webView.setOnTouchListener {view, event ->
            if(event != null) {
                //Run by gestureDetector to handle extra functionality
                gestureDetector.onTouchEvent(event)

                when(event.action) {
                    MotionEvent.ACTION_MOVE -> {
                        if(nodePressed) {
                            webView.queueEvent {
                                webRenderer.buildNodeDrag(Shapes.Point(event.x, event.y))
                            }
                            webView.requestRender()

                        } else if(regPressed) {
                            webView.queueEvent {
                                webRenderer.buildNodeDrag(Shapes.Point(event.x, event.y))
                            }
                            webView.requestRender()
                        }
                    }

                    MotionEvent.ACTION_UP -> {
                        if(nodePressed) {
                            webView.queueEvent {
                                webRenderer.buildNodeStatic(Shapes.Point(event.x, event.y))
                            }
                            webView.requestRender()

                        } else if(regPressed) {
                            webView.queueEvent {
                                webRenderer.buildNodeStatic(Shapes.Point(event.x, event.y))
                            }
                            webView.requestRender()
                        }

                        nodeTouched = false
                        nodePressed = false
                        regPressed = false
                    }
                }
                true

            } else false
        }
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


    inner class UiGestureListener : GestureDetector.SimpleOnGestureListener() {

        override fun onDown(event: MotionEvent?): Boolean {
            //TODO: consider returning node ID
            for(node in webRenderer.nodeStaticList) {

                Log.i("onDownBeforeIf", "event.x = ${event!!.x}, node.centerX = ${node.centerX}, " +
                        "event.y = ${event.y}, node.centerY = ${node.centerY}")
                Log.i("onDownBeforeIf2", "event.x - node.centerX = ${event.x - node.centerX}, " +
                        "event.y - node.centerY = ${event.y - node.centerY}")

                if(abs(event.x - node.centerX) <= Constants.RADIUS_CIRCLE
                    && abs(event.y - node.centerY) <= Constants.RADIUS_CIRCLE) {

                    nodeTouched = true
                    break
                }
            }

            return super.onDown(event)
        }

        override fun onLongPress(event: MotionEvent?) {
            if(nodeTouched) {
                nodePressed = true

            } else
                regPressed = true

            Log.i("onLongPress", "nodeTouched = $nodeTouched, nodePressed = $nodePressed, regPressed = $regPressed")
        }
    }
}
