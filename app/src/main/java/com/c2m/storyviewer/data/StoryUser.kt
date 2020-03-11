package com.c2m.storyviewer.data

import android.os.Parcelable
import com.c2m.storyviewer.data.Story
import kotlinx.android.parcel.Parcelize

@Parcelize
data class StoryUser(val username: String, val profilePicUrl: String, val stories: ArrayList<Story>) : Parcelable