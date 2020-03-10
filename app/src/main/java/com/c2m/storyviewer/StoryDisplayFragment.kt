package com.c2m.storyviewer

import android.os.Bundle
import androidx.fragment.app.Fragment

class StoryDisplayFragment : Fragment() {

    companion object {
        private const val EXTRA_POSITION = "EXTRA_POSITION"
        private const val EXTRA_STORY = "EXTRA_STORY"
        fun newInstance(position: Int, story: Story): StoryDisplayFragment {
            return StoryDisplayFragment().apply {
                arguments = Bundle().apply {
                    putInt(EXTRA_POSITION, position)
                    putParcelable(EXTRA_STORY, story)
                }
            }
        }
    }
}