package com.example.ecoapp.feature.topic

import android.os.Bundle
import android.view.View
import com.example.ecoapp.Constants
import com.example.ecoapp.R
import com.example.ecoapp.data.News
import com.example.ecoapp.feature.main.MainActivity
import com.example.ecoapp.utils.ParseHelper
import com.google.gson.Gson
import com.md.nails.presentation.basemvp.BaseMvpFragment
import com.smarteist.autoimageslider.IndicatorAnimations
import com.smarteist.autoimageslider.SliderAnimations
import com.smarteist.autoimageslider.SliderView
import kotlinx.android.synthetic.main.fragment_news_topic.*


class NewsFragment: BaseMvpFragment() {
    override val layoutId: Int
        get() = R.layout.fragment_news_topic

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val news = Gson().fromJson(arguments?.getString(Constants.NEWS_KEY),News::class.java)
        title.text = ParseHelper.parseIDLinks(news.topic)
        news_topic_text.text = ParseHelper.parseIDLinks(news.fullText)
        (activity as MainActivity).supportActionBar?.show()
        val adapter = SliderAdapterExample(activity)
        imageSlider.setSliderAdapter(adapter)
        adapter.renewItems(news.allPhotos)
        imageSlider.setIndicatorAnimation(IndicatorAnimations.WORM)
        imageSlider.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION)
        imageSlider.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_RIGHT);
        imageSlider.scrollTimeInSec = 4
        imageSlider.indicatorSelectedColor = resources.getColor(R.color.colorAccent)
        imageSlider.indicatorUnselectedColor = resources.getColor(R.color.colorPrimaryDark)
        if (news.allPhotos.size>1)
            imageSlider.startAutoCycle();
        //  (activity as MainActivity).supportActionBar?.title = news.topic
    }
}