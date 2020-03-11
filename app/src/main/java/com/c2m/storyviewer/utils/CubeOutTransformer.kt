package com.c2m.storyviewer.utils

import android.view.View
import androidx.viewpager.widget.ViewPager

class CubeOutTransformer @JvmOverloads constructor(
        private val distanceMultiplier: Int = 20
) : ViewPager.PageTransformer {

    private fun onTransform(page: View, position: Float) {
        with(page) {
            cameraDistance = (width * distanceMultiplier).toFloat()
            pivotX = if (position < 0f) width.toFloat() else 0f
            pivotY = height * 0.5f
            rotationY = 90f * position
        }
    }

    override fun transformPage(page: View, position: Float) {
        val clampedPosition = clampPosition(position)
        onPreTransform(page, clampedPosition)
        onTransform(page, clampedPosition)
    }

    private fun clampPosition(position: Float): Float {
        return when {
            position < -1f -> -1f
            position > 1f -> 1f
            else -> position
        }
    }

    private fun onPreTransform(page: View, position: Float) {
        with(page) {
            rotationX = 0f
            rotationY = 0f
            rotation = 0f
            scaleX = 1f
            scaleY = 1f
            pivotX = 0f
            pivotY = 0f
            translationY = 0f
            translationX = 0f
            alpha = if (position <= -1f || position >= 1f) 0f else 1f
            isEnabled = false
        }
    }
}