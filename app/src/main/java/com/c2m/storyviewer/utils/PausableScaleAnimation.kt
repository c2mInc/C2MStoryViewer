package com.c2m.storyviewer.utils

import android.view.animation.ScaleAnimation
import android.view.animation.Transformation

class PausableScaleAnimation internal constructor(
    fromX: Float, toX: Float, fromY: Float,
    toY: Float, pivotXType: Int, pivotXValue: Float, pivotYType: Int,
    pivotYValue: Float
) : ScaleAnimation(
    fromX, toX, fromY, toY, pivotXType, pivotXValue, pivotYType,
    pivotYValue
) {
    private var elapsedAtPause: Long = 0
    private var isPaused = false
    override fun getTransformation(
        currentTime: Long,
        outTransformation: Transformation,
        scale: Float
    ): Boolean {
        if (isPaused && elapsedAtPause == 0L) {
            elapsedAtPause = currentTime - startTime
        }
        if (isPaused) {
            startTime = currentTime - elapsedAtPause
        }
        return super.getTransformation(currentTime, outTransformation, scale)
    }

    fun pause() {
        if (isPaused) return
        elapsedAtPause = 0
        isPaused = true
    }

    fun resume() {
        isPaused = false
    }
}