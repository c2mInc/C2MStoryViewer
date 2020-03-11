package com.c2m.storyviewer

import kotlin.random.Random

object StoryGenerator {

    fun generateStories(): ArrayList<StoryUser> {
        val storyUrls = ArrayList<String>()
        storyUrls.add("https://images.wallpaperscraft.com/image/railway_train_station_134586_1080x1920.jpg")
        storyUrls.add("https://images.wallpaperscraft.com/image/road_marking_bridge_123398_1080x1920.jpg")
        storyUrls.add("https://images.wallpaperscraft.com/image/bridge_autumn_city_121892_1080x1920.jpg")
        storyUrls.add("https://images.wallpaperscraft.com/image/ruins_man_loneliness_124279_1080x1920.jpg")
        storyUrls.add("https://images.wallpaperscraft.com/image/night_city_signs_neon_139551_1080x1920.jpg")
        storyUrls.add("https://images.wallpaperscraft.com/image/tokyo_night_city_skyscrapers_121628_1080x1920.jpg")
        storyUrls.add("https://images.wallpaperscraft.com/image/new_zealand_panorama_skyscrapers_buildings_shore_lighting_119195_1080x1920.jpg")
        storyUrls.add("https://images.wallpaperscraft.com/image/city_evening_snowfall_136478_1080x1920.jpg")

        val userProfileUrls = ArrayList<String>()
        userProfileUrls.add("https://randomuser.me/api/portraits/women/10.jpg")
        userProfileUrls.add("https://randomuser.me/api/portraits/men/1.jpg")
        userProfileUrls.add("https://randomuser.me/api/portraits/women/7.jpg")
        userProfileUrls.add("https://randomuser.me/api/portraits/men/2.jpg")
        userProfileUrls.add("https://randomuser.me/api/portraits/women/3.jpg")
        userProfileUrls.add("https://randomuser.me/api/portraits/men/11.jpg")
        userProfileUrls.add("https://randomuser.me/api/portraits/women/14.jpg")
        userProfileUrls.add("https://randomuser.me/api/portraits/men/21.jpg")
        userProfileUrls.add("https://randomuser.me/api/portraits/women/28.jpg")
        userProfileUrls.add("https://randomuser.me/api/portraits/men/16.jpg")

        val storyUserList = ArrayList<StoryUser>()
        for (i in 1..10){
            val stories = ArrayList<Story>()
            val storySize = Random.nextInt(1,5)
            for (j in 0 until storySize){
                stories.add(Story(storyUrls[Random.nextInt(storyUrls.size)], System.currentTimeMillis() - (1 * (24-j) * 60 * 60 * 1000)))
            }
            storyUserList.add(StoryUser("username$i",userProfileUrls[Random.nextInt(userProfileUrls.size)],stories))
        }

        return storyUserList
    }
}