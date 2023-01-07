package cn.hayring.threelayersscrollingconflict

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import kotlin.math.abs

class LogTouchLinearLayout(
    context: Context?,
    attrs: AttributeSet?,
    defStyleAttr: Int,
    defStyleRes: Int
): LinearLayout(context, attrs, defStyleAttr, defStyleRes) {

    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : this(
        context,
        attrs,
        defStyleAttr,
        0
    )


    val targetIndex: Int

    init {
        //自定义属性
        val array: TypedArray? = context?.obtainStyledAttributes(attrs, R.styleable.LogTouchLinearLayout)
        targetIndex = array?.getInt(R.styleable.LogTouchLinearLayout_conflictTarget, 0)?:0
    }

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


    var dx = 0f

    var child: View? = null

    lateinit var viewPager: ViewPager

    lateinit var viewPager2: ViewPager2


    val location = IntArray(2)

    /**
     * only intercept target view events
     */
    private fun isTouchPointInView(view: View?, x: Float, y: Float): Boolean {
        if (view == null) {
            return false
        }
        view.getLocationOnScreen(location)
        val left = location[0].toFloat()
        val top = location[1].toFloat()
        val right = left + view.measuredWidth.toFloat()
        val bottom = top + view.measuredHeight.toFloat()
        //view.isClickable() &&
        return y in top..bottom && x in left .. right
    }

    override fun onInterceptTouchEvent(event: MotionEvent?): Boolean {
        if (event?.action == MotionEvent.ACTION_MOVE) {
            Log.v("LogTouchLinearLayout", "event: ${event.hashCode()} view: $tag action: ${event?.getName()} x: ${event?.x} y: ${event?.y}----onInterceptTouchEvent")
        } else {
            Log.d("LogTouchLinearLayout", "event: ${event.hashCode()} view: $tag action: ${event?.getName()} x: ${event?.x} y: ${event?.y}----onInterceptTouchEvent")
        }
        return super.onInterceptTouchEvent(event).let {

            if (event != null && isTouchPointInView(getChildAt(targetIndex), event.rawX, event.rawY)) {
                if (!it) {
                    //only calculate dx because NestScrollRecyclerView disallow intercept when scrolling vertically
                    dx = event.x - startX
                    Log.d("LLonInterceptTouchEvent", "dx: $dx")
                    child = getChildAt(1)
                    if (child is ViewPager2) {
                        viewPager2 = child as ViewPager2
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
                        viewPager = child as ViewPager
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
            } else {
                it
            }
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