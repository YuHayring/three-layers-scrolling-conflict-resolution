package cn.hayring.threelayersscrollingconflict

import android.util.Log
import android.view.MotionEvent
import android.view.MotionEvent.*
import android.view.View
import android.view.View.OnTouchListener

object LogOnTouchListener: OnTouchListener {
    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        log(v, event)
        val result = v?.onTouchEvent(event)!!
        logResult(v, event, result)
        return result
    }

    fun log(v: View?, event: MotionEvent?) {
        if (event?.action == MotionEvent.ACTION_MOVE) {
            Log.v("LogOnTouchListener", "event: ${event.hashCode()} view: ${v?.tag?:(v?.parent as View?)?.tag} action: ${event?.getName()} x: ${event?.x} y: ${event?.y}————onTouch")
        } else {
            Log.d("LogOnTouchListener", "event: ${event.hashCode()} view: ${v?.tag?:(v?.parent as View?)?.tag} action: ${event?.getName()} x: ${event?.x} y: ${event?.y}————onTouch")
        }
    }

    fun logResult(v: View?, event: MotionEvent?, result: Boolean) {
        Log.d("LogOnTouchListener", "event: ${event.hashCode()} view: ${v?.tag?:(v?.parent as View?)?.tag} action: ${event?.getName()} x: ${event?.x} y: ${event?.y}————onTouch return $result")
    }
}

fun MotionEvent.getName(): String {
    return when (this.action) {
        ACTION_UP -> "ACTION_UP"
        ACTION_DOWN -> "ACTION_DOWN"
        ACTION_CANCEL -> "ACTION_CANCEL"
        ACTION_MOVE -> "ACTION_MOVE"
        else -> "other"
    }
}