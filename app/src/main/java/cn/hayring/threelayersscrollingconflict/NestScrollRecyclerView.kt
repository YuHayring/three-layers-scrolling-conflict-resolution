package cn.hayring.threelayersscrollingconflict

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.abs

class NestScrollRecyclerView: RecyclerView {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )


    override fun dispatchTouchEvent(event: MotionEvent?): Boolean {
        if (event?.action == MotionEvent.ACTION_DOWN) {
            Log.v("NestScrollRV_Touch", "event: ${event.hashCode()} view: $tag action: ${event?.getName()} x: ${event?.x} y: ${event?.y}————dispatchTouchEvent——disallow")
            parent.requestDisallowInterceptTouchEvent(true)
        }
        return super.dispatchTouchEvent(event)
    }

    var startX = 0f

    var startY = 0f

    var times = 0



    companion object {
        const val tangent = 2
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        LogOnTouchListener.log(this, event)
        if (event?.action == MotionEvent.ACTION_DOWN) {
            times = 0
            startX = event.x
            startY = event.y
        }
        if (event?.action == MotionEvent.ACTION_MOVE) {
            if (event.x != startX && event.y != startY) {
                times++
            }
            if (times == 1) {
                val dx = abs(event.x - startX)
                val dy = abs(event.y - startY)
                Log.d("RVOnTouchListener", "dx: $dx, dy: $dy")
                if (tangent * dy < dx) {
                    LogTouchLinearLayout.interceptDisallowTree = true
                    parent?.requestDisallowInterceptTouchEvent(false)
                    LogTouchLinearLayout.interceptDisallowTree = false
                    LogOnTouchListener.logResult(this, event, false)
                    return false
                }
            }
        }
        return super.onTouchEvent(event).also {
            LogOnTouchListener.logResult(this, event, it)
        }
    }
}