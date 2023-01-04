package cn.hayring.threelayersscrollingconflict

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.widget.LinearLayout
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import kotlin.math.abs

class LogTouchLinearLayout: LinearLayout {

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes)


    var startX = 0f

    override fun dispatchTouchEvent(event: MotionEvent?): Boolean {
        if (event?.action == MotionEvent.ACTION_MOVE) {
            Log.v("LogTouchLinearLayout", "event: ${event.hashCode()} view: $tag action: ${event?.getName()} x: ${event?.x} y: ${event?.y}————dispatchTouchEvent")
        } else {
            Log.d("LogTouchLinearLayout", "event: ${event.hashCode()} view: $tag action: ${event?.getName()} x: ${event?.x} y: ${event?.y}————dispatchTouchEvent")
        }

        if (event?.action == MotionEvent.ACTION_DOWN) {
            startX = event.x
        }
        return super.dispatchTouchEvent(event)
    }



    override fun onInterceptTouchEvent(event: MotionEvent?): Boolean {
        if (event?.action == MotionEvent.ACTION_MOVE) {
            Log.v("LogTouchLinearLayout", "event: ${event.hashCode()} view: $tag action: ${event?.getName()} x: ${event?.x} y: ${event?.y}----onInterceptTouchEvent")
        } else {
            Log.d("LogTouchLinearLayout", "event: ${event.hashCode()} view: $tag action: ${event?.getName()} x: ${event?.x} y: ${event?.y}----onInterceptTouchEvent")
        }
        return super.onInterceptTouchEvent(event).let {
            if (!it && event != null) {
                //only calculate dx because NestScrollRecyclerView disallow intercept when scrolling vertically
                val dx = event.x - startX
                Log.d("LLonInterceptTouchEvent", "dx: $dx")
                val child = getChildAt(1)
                if (child is ViewPager2) {
                    val viewPager2 = child
                    if (dx > 0f && viewPager2.currentItem == 0 || dx < 0f && viewPager2.currentItem == viewPager2.adapter!!.itemCount - 1) {
                        LogTouchLinearLayout.interceptDisallowTree = true
                        parent?.requestDisallowInterceptTouchEvent(false)
                        LogTouchLinearLayout.interceptDisallowTree = false
                        LogOnTouchListener.logResult(this, event, false)
                        return true
                    } else {
                        return false
                    }
                } else {
                    val viewPager = child as ViewPager
                    if (dx > 0f && viewPager.currentItem == 0 || dx < 0f && viewPager.currentItem == viewPager.adapter!!.count - 1) {
                        LogTouchLinearLayout.interceptDisallowTree = true
                        parent?.requestDisallowInterceptTouchEvent(false)
                        LogTouchLinearLayout.interceptDisallowTree = false
                        LogOnTouchListener.logResult(this, event, false)
                        return true
                    } else {
                        return false
                    }
                }
            }
            return false
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event?.action == MotionEvent.ACTION_MOVE) {
            Log.v("LogTouchLinearLayout", "event: ${event.hashCode()} view: $tag action: ${event?.getName()} x: ${event?.x} y: ${event?.y}----onTouchEvent")
        } else {
            Log.d("LogTouchLinearLayout", "event: ${event.hashCode()} view: $tag action: ${event?.getName()} x: ${event?.x} y: ${event?.y}----onTouchEvent")
        }
        return super.onTouchEvent(event)
    }


    override fun requestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {
        super.requestDisallowInterceptTouchEvent(disallowIntercept)
        if (interceptDisallowTree) {
            parent.requestDisallowInterceptTouchEvent(true)
        }
    }

    companion object {
        var interceptDisallowTree = false
    }
}