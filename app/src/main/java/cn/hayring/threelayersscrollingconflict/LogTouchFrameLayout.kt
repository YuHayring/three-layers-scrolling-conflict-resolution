package cn.hayring.threelayersscrollingconflict

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.widget.FrameLayout

class LogTouchFrameLayout: FrameLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes)


    override fun dispatchTouchEvent(event: MotionEvent?): Boolean {
        if (event?.action == MotionEvent.ACTION_MOVE) {
            Log.v("LogTouchLinearLayout", "event: ${event.hashCode()} view: $tag action: ${event?.getName()} x: ${event?.x} y: ${event?.y}————dispatchTouchEvent")
        } else {
            Log.d("LogTouchLinearLayout", "event: ${event.hashCode()} view: $tag action: ${event?.getName()} x: ${event?.x} y: ${event?.y}————dispatchTouchEvent")
        }
        return super.dispatchTouchEvent(event)
    }

    override fun onInterceptTouchEvent(event: MotionEvent?): Boolean {
        if (event?.action == MotionEvent.ACTION_MOVE) {
            Log.v("LogTouchLinearLayout", "event: ${event.hashCode()} view: $tag action: ${event?.getName()} x: ${event?.x} y: ${event?.y}----onInterceptTouchEvent")
        } else {
            Log.d("LogTouchLinearLayout", "event: ${event.hashCode()} view: $tag action: ${event?.getName()} x: ${event?.x} y: ${event?.y}----onInterceptTouchEvent")
        }
        return super.onInterceptTouchEvent(event)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event?.action == MotionEvent.ACTION_MOVE) {
            Log.v("LogTouchLinearLayout", "event: ${event.hashCode()} view: $tag action: ${event?.getName()} x: ${event?.x} y: ${event?.y}----onTouchEvent")
        } else {
            Log.d("LogTouchLinearLayout", "event: ${event.hashCode()} view: $tag action: ${event?.getName()} x: ${event?.x} y: ${event?.y}----onTouchEvent")
        }
        return super.onTouchEvent(event)
    }
}