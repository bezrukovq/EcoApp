package com.example.ecoapp.feature.intro

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.ecoapp.R
import com.example.ecoapp.feature.main.MainActivity
import com.example.ecoapp.utils.PreferencesManager
import com.github.paolorotolo.appintro.AppIntro2
import com.github.paolorotolo.appintro.AppIntro2Fragment
import com.github.paolorotolo.appintro.model.SliderPagerBuilder

class WelcomeActivity : AppIntro2() {

    private lateinit var manager: PreferencesManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        manager = PreferencesManager(this)
        if (manager.isFirstRun()) {
            showIntroSlides()
        } else {
            goToMain()
        }
    }

    private fun showIntroSlides() {
        manager.setFirstRun()
        val pageOne = SliderPagerBuilder()
            .title(getString(R.string.intro_title))
            .description("Узнавать актуальные новости")
            .imageDrawable(R.drawable.tutor_1)
            .bgColor(resources.getColor(R.color.colorAccent))
            .build()
        val pageTwo = SliderPagerBuilder()
            .title(getString(R.string.intro_title))
            .description("Развить свою экологическую грамотность")
            .imageDrawable(R.drawable.tutor_2)
            .bgColor(resources.getColor(R.color.colorAccent))
            .build()
        val pageThree = SliderPagerBuilder()
            .title(getString(R.string.intro_title))
            .description("Узнать где принимают мусор в твоем городе")
            .imageDrawable(R.drawable.tutor_3)
            .bgColor(resources.getColor(R.color.colorAccent))
            .build()
        addSlide(AppIntro2Fragment.newInstance(pageOne))
        addSlide(AppIntro2Fragment.newInstance(pageTwo))
        addSlide(AppIntro2Fragment.newInstance(pageThree))
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