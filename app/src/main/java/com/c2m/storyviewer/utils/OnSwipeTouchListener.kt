package com.c2m.storyviewer.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import kotlin.math.abs

open class OnSwipeTouchListener(context: Activity) : View.OnTouchListener {

    private val gestureDetector: GestureDetector
    private var startX: Float = 0f
    private var startY: Float = 0f
    private var touchDownTime = 0L

    init {
        gestureDetector = GestureDetector(context, GestureListener())
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouch(view: View, event: MotionEvent): Boolean {
        onTouchView(view, event)
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                touchDownTime = now()
                startX = event.x
                startY = event.y
            }
            MotionEvent.ACTION_UP -> {
                val endX = event.x
                val endY = event.y
                if (isAClick(startX, endX, startY, endY)) {
                    onClick(view)
                }
            }
        }
        return gestureDetector.onTouchEvent(event)
    }

    private fun isAClick(startX: Float, endX: Float, startY: Float, endY: Float): Boolean {
        val isTouchDuration = now() - touchDownTime < CLICK_TIME_THRESHOLD  // short time should mean this is a click
        val isTouchLength = abs(endX - startX) + abs(endY - startY) < TOUCH_MOVE_THRESHOLD  // short length should mean this is a click
        return isTouchDuration && isTouchLength
    }

    private inner class GestureListener : GestureDetector.SimpleOnGestureListener() {

        override fun onDown(e: MotionEvent): Boolean {
            return true
        }

        override fun onFling(e1: MotionEvent, e2: MotionEvent, velocityX: Float, velocityY: Float): Boolean {
            var result = false
            try {
                val diffY = e2.y - e1.y
                if (abs(diffY) > SWIPE_THRESHOLD && abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                    if (diffY > 0) {
                        onSwipeBottom()
                    } else {
                        onSwipeTop()
                    }
                    result = true
                }
            } catch (exception: Exception) {
                exception.printStackTrace()
            }
            return result
        }

        override fun onLongPress(e: MotionEvent?) {
            onLongClick()
        }
    }

    private fun now() = System.currentTimeMillis()

    open fun onClick(view: View) {}

    open fun onLongClick() {}

    open fun onTouchView(view: View, event: MotionEvent): Boolean {
        return false
    }

    open fun onSwipeTop() {}

    open fun onSwipeBottom() {}

    companion object {
        private const val SWIPE_THRESHOLD = 100
        private const val SWIPE_VELOCITY_THRESHOLD = 100
        private const val CLICK_TIME_THRESHOLD = 200L
        private const val TOUCH_MOVE_THRESHOLD = 150L
    }
}