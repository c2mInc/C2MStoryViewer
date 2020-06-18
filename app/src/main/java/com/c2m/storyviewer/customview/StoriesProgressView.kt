package com.c2m.storyviewer.customview

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import com.c2m.storyviewer.R
import java.util.*

class StoriesProgressView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : LinearLayout(context, attrs, defStyle) {

    private val progressBars: MutableList<PausableProgressBar> = ArrayList()
    private var storiesListener: StoriesListener? = null
    private var storiesCount = -1
    private var current = -1
    private var isSkipStart = false
    private var isReverseStart = false
    private var position = -1
    private var isComplete = false

    init {
        orientation = HORIZONTAL
        val typedArray = context.obtainStyledAttributes(attrs,
            R.styleable.StoriesProgressView
        )
        storiesCount = typedArray.getInt(R.styleable.StoriesProgressView_progressCount, 0)
        typedArray.recycle()
        bindViews()
    }

    private fun bindViews() {
        progressBars.clear()
        removeAllViews()
        for (i in 0 until storiesCount) {
            val p = createProgressBar()
            p.tag = "p($position) c($i)" // debug
            progressBars.add(p)
            addView(p)
            if (i + 1 < storiesCount) {
                addView(createSpace())
            }
        }
    }

    private fun createProgressBar(): PausableProgressBar {
        return PausableProgressBar(context).apply {
            layoutParams =
                PROGRESS_BAR_LAYOUT_PARAM
        }
    }

    private fun createSpace(): View {
        return View(context).apply {
            layoutParams =
                SPACE_LAYOUT_PARAM
        }
    }

    private fun callback(index: Int): PausableProgressBar.Callback {
        return object : PausableProgressBar.Callback {
            override fun onStartProgress() {
                current = index
            }

            override fun onFinishProgress() {
                if (isReverseStart) {
                    if (storiesListener != null) storiesListener!!.onPrev()
                    if (0 <= current - 1) {
                        val p = progressBars[current - 1]
                        p.setMinWithoutCallback()
                        progressBars[--current].startProgress()
                    } else {
                        progressBars[current].startProgress()
                    }
                    isReverseStart = false
                    return
                }
                val next = current + 1
                if (next <= progressBars.size - 1) {
                    if (storiesListener != null) storiesListener!!.onNext()
                    progressBars[next].startProgress()
                    ++current
                } else {
                    isComplete = true
                    if (storiesListener != null) storiesListener!!.onComplete()
                }
                isSkipStart = false
            }
        }
    }

    fun setStoriesCountDebug(storiesCount: Int, position: Int) {
        this.storiesCount = storiesCount
        this.position = position
        bindViews()
    }

    fun setStoriesListener(storiesListener: StoriesListener?) {
        this.storiesListener = storiesListener
    }

    fun skip() {
        if (isSkipStart || isReverseStart) return
        if (isComplete) return
        if (current < 0) return
        val p = progressBars[current]
        isSkipStart = true
        p.setMax()
    }

    fun reverse() {
        if (isSkipStart || isReverseStart) return
        if (isComplete) return
        if (current < 0) return
        val p = progressBars[current]
        isReverseStart = true
        p.setMin()
    }

    fun setAllStoryDuration(duration: Long) {
        for (i in progressBars.indices) {
            progressBars[i].setDuration(duration)
            progressBars[i].setCallback(callback(i))
        }
    }

    fun startStories() {
        if (progressBars.size > 0) {
            progressBars[0].startProgress()
        }
    }

    fun startStories(from: Int) {
        for (i in progressBars.indices) {
            progressBars[i].clear()
        }
        for (i in 0 until from) {
            if (progressBars.size > i) {
                progressBars[i].setMaxWithoutCallback()
            }
        }
        if (progressBars.size > from) {
            progressBars[from].startProgress()
        }
    }

    fun destroy() {
        for (p in progressBars) {
            p.clear()
        }
    }

    fun abandon() {
        if (progressBars.size > current && current >= 0) {
            progressBars[current].setMinWithoutCallback()
        }
    }

    fun pause() {
        if (current < 0) return
        progressBars[current].pauseProgress()
    }

    fun resume() {
        if (current < 0 && progressBars.size > 0) {
            progressBars[0].startProgress()
            return
        }
        progressBars[current].resumeProgress()
    }

    fun getProgressWithIndex(index: Int): PausableProgressBar {
        return progressBars[index]
    }

    companion object {
        private val PROGRESS_BAR_LAYOUT_PARAM = LayoutParams(0, LayoutParams.WRAP_CONTENT, 1F)
        private val SPACE_LAYOUT_PARAM = LayoutParams(5, LayoutParams.WRAP_CONTENT)
    }

    interface StoriesListener {
        fun onNext()
        fun onPrev()
        fun onComplete()
    }
}