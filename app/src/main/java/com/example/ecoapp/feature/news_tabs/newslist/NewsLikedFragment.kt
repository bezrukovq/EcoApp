package com.example.ecoapp.feature.news_tabs.newslist

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide
import com.example.ecoapp.Constants
import com.example.ecoapp.R
import com.example.ecoapp.data.News
import com.example.ecoapp.feature.main.MainActivity
import com.example.ecoapp.feature.news_tabs.NewsTab
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.md.nails.presentation.basemvp.BaseMvpFragment
import kotlinx.android.synthetic.main.fragment_news_list.*

class NewsLikedFragment: BaseMvpFragment(), NewsTab, SwipeRefreshLayout.OnRefreshListener {
    override val layoutId: Int
        get() = R.layout.fragment_news_list

    private var adapter : NewsAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        retainInstance =true
        adapter = NewsAdapter(Glide.with(this)){ news, liked-> onLikePressed(news,liked)}
        news_list.adapter = adapter
        refresh()
        swipe_refresh.setOnRefreshListener(this)
        (activity as MainActivity).supportActionBar?.hide()
    }

    override fun refresh(){
        feed_loading.visibility = View.VISIBLE
        adapter?.newsList?.clear()
        val prefs = activity?.getSharedPreferences(Constants.SHAREDPREF, Context.MODE_PRIVATE)
        val list = Gson().fromJson(prefs?.getString(Constants.LIKED_LIST,""),Constants.NEWS_LIST_TYPE)?:ArrayList<News>()
        adapter?.newsList = list
        toBeLikedHint.visibility = if (list.isEmpty()) View.VISIBLE else View.GONE
        adapter?.notifyDataSetChanged()
        feed_loading.visibility = View.GONE
        swipe_refresh.isRefreshing = false
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

    override fun getTitle() = "Избранное"
    override fun onRefresh() {
        swipe_refresh.isRefreshing = true
        refresh()
    }
}