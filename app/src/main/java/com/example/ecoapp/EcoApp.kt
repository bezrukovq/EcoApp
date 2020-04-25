package com.example.ecoapp

import android.app.Application
import com.vk.sdk.VKSdk
import com.vk.sdk.api.*
import com.vk.sdk.api.model.VKApiPoll
import com.vk.sdk.api.model.VKApiPost
import com.vk.sdk.api.model.VKList
import com.vk.sdk.util.VKUtil

class EcoApp: Application() {
    override fun onCreate() {
        super.onCreate()
        VKSdk.initialize(applicationContext)
    }
}
