package cn.hayring.threelayersscrollingconflict

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.lang.Math.signum
import kotlin.math.abs
import kotlin.math.sign

class NestScrollHorizontalRecyclerView: RecyclerView {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )


    override fun dispatchTouchEvent(event: MotionEvent?): Boolean {
        if (event?.action == MotionEvent.ACTION_DOWN) {
            Log.v("NestScrollRVH_Touch", "event: ${event.hashCode()} view: $tag action: ${event?.getName()} x: ${event?.x} y: ${event?.y}————dispatchTouchEvent——disallow")
            parent.requestDisallowInterceptTouchEvent(true)
            startX = event.x
            solve = false
        }
        if (!solve && event?.action == MotionEvent.ACTION_MOVE) {
            if (event.x != startX) {
                dx = event.x - startX
                Log.d("RVHOnTouchListener", "dx: $dx")
                (layoutManager as LinearLayoutManager).let {
                    if (adapter != null) {
                        if (it.findFirstCompletelyVisibleItemPosition() == 0 && dx > 0
                            || it.findLastCompletelyVisibleItemPosition() == adapter!!.itemCount - 1 && dx < 0) {
                            LogTouchLinearLayout.interceptDisallowTree = true
                            parent?.requestDisallowInterceptTouchEvent(false)
                            LogTouchLinearLayout.interceptDisallowTree = false
                            Log.v("NestScrollRVH_Touch", "event: ${event.hashCode()} view: $tag action: ${event?.getName()} x: ${event?.x} y: ${event?.y}————dispatchTouchEvent——return-false")
                            Log.d("RVHOnTouchListener", "requestDisallowInterceptTouchEvent")
                            solve = true
                            return false
                        }
                    }
                }
                solve = true
            }
        }
        return super.dispatchTouchEvent(event)
    }

    var startX = 0f

    var dx = 0f



    var solve = false

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        LogOnTouchListener.log(this, event)
        return super.onTouchEvent(event).also {
            LogOnTouchListener.logResult(this, event, it)
        }
    }
}