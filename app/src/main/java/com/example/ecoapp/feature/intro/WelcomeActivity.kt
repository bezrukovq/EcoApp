package com.example.ecoapp.feature.intro

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.ecoapp.R
import com.example.ecoapp.feature.main.MainActivity
import com.github.paolorotolo.appintro.AppIntro2
import com.github.paolorotolo.appintro.AppIntro2Fragment
import com.github.paolorotolo.appintro.model.SliderPagerBuilder

class WelcomeActivity : AppIntro2() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        showIntroSlides()
    }

    private fun showIntroSlides() {
        val pageOne = SliderPagerBuilder()
            .title(getString(R.string.intro_title))
            .description("Desc desc desc desc desc desc desc desc desc desc")
            .imageDrawable(R.drawable.icon_markings)
            .bgColor(resources.getColor(R.color.colorAccent))
            .build()
        val pageTwo = SliderPagerBuilder()
            .title(getString(R.string.intro_title))
            .description("Desc desc desc desc desc desc desc desc desc desc")
            .imageDrawable(R.drawable.img_topic)
            .bgColor(resources.getColor(R.color.colorAccent))
            .build()
        addSlide(AppIntro2Fragment.newInstance(pageOne))
        addSlide(AppIntro2Fragment.newInstance(pageTwo))
    }
    private fun goToMain() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
    override fun onSkipPressed(currentFragment: Fragment?) {
        super.onSkipPressed(currentFragment)
        goToMain()
    }
    override fun onDonePressed(currentFragment: Fragment?) {
        super.onDonePressed(currentFragment)
        goToMain()
    }

}