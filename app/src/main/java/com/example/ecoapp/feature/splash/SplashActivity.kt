package com.example.ecoapp.feature.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.example.ecoapp.feature.intro.WelcomeActivity
import com.vk.sdk.util.VKUtil


class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Handler().postDelayed({
            startActivity(Intent(this,
                WelcomeActivity::class.java))
            finish()
        },500)
    }
}
