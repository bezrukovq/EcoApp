package com.example.ecoapp.feature.topic

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.example.ecoapp.Constants
import com.example.ecoapp.R
import com.example.ecoapp.data.News
import com.example.ecoapp.feature.main.MainActivity
import com.example.ecoapp.feature.news_tabs.newslist.NewsAdapter
import com.example.ecoapp.utils.ParseHelper
import com.google.gson.Gson
import com.md.nails.presentation.basemvp.BaseMvpFragment
import com.smarteist.autoimageslider.IndicatorAnimations
import com.smarteist.autoimageslider.SliderAnimations
import com.smarteist.autoimageslider.SliderView
import kotlinx.android.synthetic.main.fragment_news_list.*
import kotlinx.android.synthetic.main.fragment_news_topic.*
import kotlinx.android.synthetic.main.fragment_news_topic.news_list
import kotlinx.android.synthetic.main.item_news_list.view.*


class NewsFragment: BaseMvpFragment() {
    override val layoutId: Int
        get() = R.layout.fragment_news_topic

    private var newsAdapter : NewsAdapter? = null

    private var initedNews: News? = null

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
        val prefs = activity?.getSharedPreferences(Constants.SHAREDPREF, Context.MODE_PRIVATE)
        val list = Gson().fromJson(prefs?.getString(Constants.LIKED_LIST,""),Constants.NEWS_LIST_TYPE)?:ArrayList<News>()
        news.liked = list.contains(news)
        if (news.liked)
            topic_like.progress = 1f
        else
            topic_like.progress = 0f
        fab_like.setOnClickListener {
            if (news.liked)
                topic_like.speed = -1f
            else
                topic_like.speed = 1f
            news.liked = !news.liked
            topic_like.playAnimation()
            onLikePressed(news,news.liked)
        }
        newsAdapter = NewsAdapter(Glide.with(this)){ itnews, liked-> onLikePressed(itnews,liked)}
        news_list.adapter = newsAdapter
        newsAdapter?.newsList = news.attachedNews
        attached.visibility = if(news.attachedNews.isEmpty()) View.GONE else View.VISIBLE
        initedNews = news
    }

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).supportActionBar?.title = initedNews?.topic?.apply {
            subSequence(0,if (length<120 ){ length }else{20}).toString().plus("...")
        }
    }

    @SuppressLint("ApplySharedPref")
    private fun onLikePressed(news: News, liked: Boolean){
        val prefs = activity?.getSharedPreferences(Constants.SHAREDPREF, Context.MODE_PRIVATE)
        val list = Gson().fromJson(prefs?.getString(Constants.LIKED_LIST,""),Constants.NEWS_LIST_TYPE)?:ArrayList<News>()
        if (liked) {
            if (!list.contains(news)) {
                list.add(news)
            }
        }
        else
            if (list.contains(news)){
                list.remove(news)
            }
        prefs?.edit()?.putString(Constants.LIKED_LIST,Gson().toJson(list))?.commit()
    }
}