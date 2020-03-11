package com.c2m.storyviewer.screen

import android.os.Handler
import android.util.Log
import androidx.viewpager.widget.ViewPager.*

abstract class PageChangeListener : OnPageChangeListener {

    private var pageBeforeDragging = 0
    private var currentPage = 0
    private var lastTime = DEBOUNCE_TIMES + 1L

    override fun onPageScrollStateChanged(state: Int) {
        when (state) {
            SCROLL_STATE_IDLE -> {
                Log.d("onPageScrollState"," SCROLL_STATE_IDLE")
                val now = System.currentTimeMillis()
                if (now - lastTime < DEBOUNCE_TIMES) {
                    return
                }
                lastTime = now
                Handler().postDelayed({
                    if (pageBeforeDragging == currentPage) {
                        onPageScrollCanceled()
                    }
                }, 300L)
            }
            SCROLL_STATE_DRAGGING -> {
                Log.d("onPageScrollState"," SCROLL_STATE_DRAGGING")
                pageBeforeDragging = currentPage
            }
            SCROLL_STATE_SETTLING -> {
                Log.d("onPageScrollState"," SCROLL_STATE_SETTLING")
            }
        }
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
    }

    override fun onPageSelected(position: Int) {
        Log.d("onPageScrollState","onPageSelected(): position($position)")
        currentPage = position
    }

    abstract fun onPageScrollCanceled()

    companion object {
        private const val DEBOUNCE_TIMES = 500L
    }
}
