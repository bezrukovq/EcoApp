package com.example.ecoapp

import com.example.ecoapp.data.News
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

object Constants {
    const val NEWS_KEY = "news_key&312"
    const val VK_API_ACCESS_TOKEN = "4703d9d04703d9d04703d9d0964773eaa8447034703d9d01969fd13b0ee140d5992dc7e"
    const val SHAREDPREF = "com.ecoapp.SharedPreferences"
    const val LIKED_LIST = "com.ecoapp.LikedList"
    val NEWS_LIST_TYPE = object : TypeToken<ArrayList<News>>() {}.type
}