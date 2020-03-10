package com.c2m.storyviewer

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Story(
    /*@SerializedName("url")*/ val url: String,
    /*@SerializedName("story_date")*/ val storyDate: Long
) : Parcelable {

    fun getElapsedTime(): Long {
        return System.currentTimeMillis() / 1000 - storyDate
    }

    fun isVideo() = url.endsWith(".mp4")

    fun isImage() = url.endsWith(".jpg")

}