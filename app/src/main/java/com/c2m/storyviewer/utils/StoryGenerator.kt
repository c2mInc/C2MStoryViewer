package com.c2m.storyviewer.utils

import com.c2m.storyviewer.data.Story
import com.c2m.storyviewer.data.StoryUser
import kotlin.random.Random

object StoryGenerator {

    fun generateStories(): ArrayList<StoryUser> {
        val storyUrls = ArrayList<String>()
        storyUrls.add("https://player.vimeo.com/external/397941817.sd.mp4?s=e5caa44dc8144b2e16f512d5a2252eafd7a57cb9")
        storyUrls.add("https://player.vimeo.com/external/398518760.sd.mp4?s=74ee9c2459c035f5bbbb8775062318cdf4b8899e")
        storyUrls.add("https://player.vimeo.com/external/403270649.sd.mp4?s=e43d1030b321cf7dcc7641ce05dfa5af650f212b")
        storyUrls.add("https://player.vimeo.com/external/403274150.sd.mp4?s=1c8131bbd60b70810584b1b9828be6d7e4ad60b0")
        storyUrls.add("https://player.vimeo.com/external/403652508.sd.mp4?s=f6fff2f196fc5899d730fd9af5544b7e6fbc5aa6")
        storyUrls.add("https://player.vimeo.com/external/399877519.sd.mp4?s=a76471b36e6cbe37bf7e73b6fb9509b2a605f9ec")
        storyUrls.add("https://images.pexels.com/photos/1366919/pexels-photo-1366919.jpeg?auto=compress&cs=tinysrgb&dpr=3&h=750&w=1260")
        storyUrls.add("https://images.pexels.com/photos/1236701/pexels-photo-1236701.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260")
        storyUrls.add("https://images.pexels.com/photos/1624496/pexels-photo-1624496.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260")
        storyUrls.add("https://images.pexels.com/photos/1535162/pexels-photo-1535162.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260")
        storyUrls.add("https://images.pexels.com/photos/799443/pexels-photo-799443.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260")
        storyUrls.add("https://images.pexels.com/photos/1433052/pexels-photo-1433052.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260")
        storyUrls.add("https://images.pexels.com/photos/1366630/pexels-photo-1366630.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260")
        storyUrls.add("https://images.pexels.com/photos/1067333/pexels-photo-1067333.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260")
        storyUrls.add("https://images.pexels.com/photos/1122868/pexels-photo-1122868.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260")
        storyUrls.add("https://images.pexels.com/photos/1837603/pexels-photo-1837603.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260")
        storyUrls.add("https://images.pexels.com/photos/1612461/pexels-photo-1612461.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260")
        storyUrls.add("https://images.pexels.com/photos/1591382/pexels-photo-1591382.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260")
        storyUrls.add("https://images.pexels.com/photos/53757/pexels-photo-53757.jpeg?auto=compress&cs=tinysrgb&dpr=2&w=500")
        storyUrls.add("https://images.pexels.com/photos/134020/pexels-photo-134020.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260")
        storyUrls.add("https://images.pexels.com/photos/1367067/pexels-photo-1367067.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260")
        storyUrls.add("https://images.pexels.com/photos/1420226/pexels-photo-1420226.jpeg?auto=compress&cs=tinysrgb&dpr=2&w=500")
        storyUrls.add("https://images.pexels.com/photos/2217365/pexels-photo-2217365.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260")
        storyUrls.add("https://images.pexels.com/photos/2260800/pexels-photo-2260800.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260")
        storyUrls.add("https://images.pexels.com/photos/1719344/pexels-photo-1719344.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260")
        storyUrls.add("https://images.pexels.com/photos/364096/pexels-photo-364096.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260")
        storyUrls.add("https://images.pexels.com/photos/3849168/pexels-photo-3849168.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260")
        storyUrls.add("https://images.pexels.com/photos/2953901/pexels-photo-2953901.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260")
        storyUrls.add("https://images.pexels.com/photos/3538558/pexels-photo-3538558.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260")
        storyUrls.add("https://images.pexels.com/photos/2458400/pexels-photo-2458400.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260")

        val userProfileUrls = ArrayList<String>()
        userProfileUrls.add("https://randomuser.me/api/portraits/women/1.jpg")
        userProfileUrls.add("https://randomuser.me/api/portraits/men/1.jpg")
        userProfileUrls.add("https://randomuser.me/api/portraits/women/2.jpg")
        userProfileUrls.add("https://randomuser.me/api/portraits/men/2.jpg")
        userProfileUrls.add("https://randomuser.me/api/portraits/women/3.jpg")
        userProfileUrls.add("https://randomuser.me/api/portraits/men/3.jpg")
        userProfileUrls.add("https://randomuser.me/api/portraits/women/4.jpg")
        userProfileUrls.add("https://randomuser.me/api/portraits/men/4.jpg")
        userProfileUrls.add("https://randomuser.me/api/portraits/women/5.jpg")
        userProfileUrls.add("https://randomuser.me/api/portraits/men/5.jpg")
        userProfileUrls.add("https://randomuser.me/api/portraits/women/6.jpg")
        userProfileUrls.add("https://randomuser.me/api/portraits/men/6.jpg")
        userProfileUrls.add("https://randomuser.me/api/portraits/women/7.jpg")
        userProfileUrls.add("https://randomuser.me/api/portraits/men/7.jpg")
        userProfileUrls.add("https://randomuser.me/api/portraits/women/8.jpg")
        userProfileUrls.add("https://randomuser.me/api/portraits/men/8.jpg")
        userProfileUrls.add("https://randomuser.me/api/portraits/women/9.jpg")
        userProfileUrls.add("https://randomuser.me/api/portraits/men/9.jpg")
        userProfileUrls.add("https://randomuser.me/api/portraits/women/10.jpg")
        userProfileUrls.add("https://randomuser.me/api/portraits/men/10.jpg")
        userProfileUrls.add("https://randomuser.me/api/portraits/women/11.jpg")
        userProfileUrls.add("https://randomuser.me/api/portraits/men/11.jpg")

        val storyUserList = ArrayList<StoryUser>()
        for (i in 1..10) {
            val stories = ArrayList<Story>()
            val storySize = Random.nextInt(1, 5)
            for (j in 0 until storySize) {
                stories.add(
                    Story(
                        storyUrls[Random.nextInt(storyUrls.size)],
                        System.currentTimeMillis() - (1 * (24 - j) * 60 * 60 * 1000)
                    )
                )
            }
            storyUserList.add(
                StoryUser(
                    "username$i",
                    userProfileUrls[Random.nextInt(userProfileUrls.size)],
                    stories
                )
            )
        }

        return storyUserList
    }
}