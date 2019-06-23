package vs.test.aviasales.ui

import android.R
import android.app.Activity
import android.content.Context
import android.os.Handler
import android.os.SystemClock
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.content.ContextCompat.getSystemService

private object TouchLocker {
    internal var lastTouchTime: Long = 0
    internal val TOUCH_FREEZE_TIME = 300L
}

fun View.setKeyboardFocusView() {
    (Handler()).postDelayed({
        requestFocus()
        (context.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager)
            ?.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
    }, 150)
}

fun hideKeyboard(activity: Activity) {
    val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(activity.window.decorView.findViewById<View>(R.id.content).windowToken, 0)
}

fun View.toggleGone(visible: Boolean) {
    visibility = if (visible) View.VISIBLE else View.GONE
}

fun View.setGone() {
    this.visibility = View.GONE
}

fun View.setVisible() {
    this.visibility = View.VISIBLE
}

fun singleClick(delay: Long = TouchLocker.TOUCH_FREEZE_TIME, touchEvent: () -> Unit) {
    val currentTime = System.currentTimeMillis()
    if (currentTime - delay < TouchLocker.lastTouchTime) return

    TouchLocker.lastTouchTime = currentTime
    touchEvent.invoke()
}

class MotionTransition(
    private val onTrigger : (() -> Unit)? = null,
    private val onStart : (() -> Unit)? = null,
    private val onChange : (() -> Unit)? = null,
    private val onCompleted : (() -> Unit)? = null
): MotionLayout.TransitionListener {
    override fun onTransitionTrigger(p0: MotionLayout?, p1: Int, p2: Boolean, p3: Float) {
        onTrigger?.invoke()
    }

    override fun onTransitionStarted(p0: MotionLayout?, p1: Int, p2: Int) {
        onStart?.invoke()
    }

    override fun onTransitionChange(p0: MotionLayout?, p1: Int, p2: Int, p3: Float) {
        onChange?.invoke()
    }

    override fun onTransitionCompleted(p0: MotionLayout?, p1: Int) {
        onCompleted?.invoke()
    }
}

fun View.scaleTap() {
    setOnTouchListener { v, event ->
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                v.animate().scaleX(0.9f).setDuration(100).start()
                v.animate().scaleY(0.9f).setDuration(100).start()
                return@setOnTouchListener false
            }
            MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP -> {
                v.animate().cancel()
                v.animate().scaleX(1f).setDuration(200).start()
                v.animate().scaleY(1f).setDuration(200).start()
                return@setOnTouchListener false
            }
            else -> {
                false
            }
        }
    }
}