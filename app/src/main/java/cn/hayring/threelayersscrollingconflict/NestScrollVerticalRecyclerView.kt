package cn.hayring.threelayersscrollingconflict

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.abs

class NestScrollVerticalRecyclerView: RecyclerView {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )


    override fun dispatchTouchEvent(event: MotionEvent?): Boolean {
        if (event?.action == MotionEvent.ACTION_DOWN) {
            solve = false
            startX = event.x
            startY = event.y
            Log.v("NestScrollRVV_Touch", "event: ${event.hashCode()} view: $tag action: ${event?.getName()} x: ${event?.x} y: ${event?.y}————dispatchTouchEvent——disallow")
            parent.requestDisallowInterceptTouchEvent(true)
        }

        if (!solve && event?.action == MotionEvent.ACTION_MOVE) {
            if (event.x != startX && event.y != startY) {
                dx = abs(event.x - startX)
                dy = abs(event.y - startY)
                Log.d("RVVOnTouchListener", "dx: $dx, dy: $dy")
                if (tangent * dy < dx) {
                    LogTouchLinearLayout.interceptDisallowTree = true
                    parent?.requestDisallowInterceptTouchEvent(false)
                    LogTouchLinearLayout.interceptDisallowTree = false
                    Log.v("NestScrollRVV_Touch", "event: ${event.hashCode()} view: $tag action: ${event?.getName()} x: ${event?.x} y: ${event?.y}————dispatchTouchEvent——return-false")
                    Log.d("RVVOnTouchListener", "requestDisallowInterceptTouchEvent")
                    solve = true
                    return false
                }
                solve = true
            }
        }
        return super.dispatchTouchEvent(event)
    }

    var startX = 0f

    var startY = 0f

    var dx = 0f

    var dy = 0f



    companion object {
        const val tangent = 2
    }


    var solve = false

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        LogOnTouchListener.log(this, event)
        return super.onTouchEvent(event).also {
            LogOnTouchListener.logResult(this, event, it)
        }
    }
}