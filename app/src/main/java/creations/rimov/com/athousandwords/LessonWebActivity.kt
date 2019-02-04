package creations.rimov.com.athousandwords

import android.annotation.SuppressLint
import android.app.ActivityManager
import android.content.Context
import android.opengl.GLSurfaceView
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.widget.Toast
import creations.rimov.com.athousandwords.objects.Shapes
import creations.rimov.com.athousandwords.util.GeneralUtil

class LessonWebActivity : AppCompatActivity() {

    private lateinit var webView: GLSurfaceView
    private val webRenderer = WebRenderer(this)

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        webView = GLSurfaceView(this)

        val configInfo = (getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager)
            .deviceConfigurationInfo

        if (configInfo.reqGlEsVersion >= 0x20000) {
            webView.setEGLContextClientVersion(2)
            webView.setRenderer(webRenderer)
            webView.renderMode = GLSurfaceView.RENDERMODE_WHEN_DIRTY

        } else
            Toast.makeText(this, "Wrong OpenGL ES version.", Toast.LENGTH_SHORT).show()

        setContentView(webView)

        webView.setOnTouchListener { view, event ->
            if(event != null) {
                var nodeTouched = false

                when(event.action) {

                    MotionEvent.ACTION_DOWN -> {
                        nodeTouched = true
                        webView.queueEvent {
                            Log.i("LessonWebActivity", "\n\nevent x = ${event.x}, event y = ${event.y} " +
                                    "\nView width = ${view.measuredWidth}, view height = ${view.measuredHeight}")

                            webRenderer.buildNodeDrag(
                                GeneralUtil.pixelsToPoint(event.x, event.y, view.measuredWidth, view.measuredHeight))
                            webView.requestRender()
                        }
                    }

                    MotionEvent.ACTION_MOVE -> {
                        webView.queueEvent {
                            webRenderer.buildNodeDrag(
                                GeneralUtil.pixelsToPoint(event.x, event.y, view.measuredWidth, view.measuredHeight))
                        }
                        webView.requestRender()
                    }

                    MotionEvent.ACTION_UP -> {
                        nodeTouched = false

                        webView.queueEvent {
                            webRenderer.buildNodeStatic(
                                GeneralUtil.pixelsToPoint(event.x, event.y, view.measuredWidth, view.measuredHeight))
                        }
                        webView.requestRender()
                    }
                }
                true

            } else
                false
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

}
