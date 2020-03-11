package com.c2m.storyviewer

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Story(
    val url: String,
    val storyDate: Long
) : Parcelable {

    fun getElapsedTime(): Long {
        return System.currentTimeMillis() / 1000 - storyDate
    }

}