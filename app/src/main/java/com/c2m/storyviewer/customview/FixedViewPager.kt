package com.c2m.storyviewer.customview

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent

import androidx.viewpager.widget.ViewPager

class FixedViewPager @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) :
    ViewPager(context, attrs) {
    // prevent NPE if fake dragging and touching ViewPager
    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        return if (isFakeDragging) {
            false
        } else try {
            super.onInterceptTouchEvent(ev)
        } catch (e: IllegalArgumentException) {
            false
        }
    }
}