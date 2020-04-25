package com.example.ecoapp.feature.news_tabs.newslist

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide
import com.example.ecoapp.Constants
import com.example.ecoapp.feature.main.MainActivity
import com.example.ecoapp.R
import com.example.ecoapp.data.News
import com.example.ecoapp.feature.news_tabs.NewsTab
import com.google.gson.Gson
import com.md.nails.presentation.basemvp.BaseMvpFragment
import com.vk.sdk.api.*
import com.vk.sdk.api.model.VKApiPhoto
import com.vk.sdk.api.model.VKApiPost
import com.vk.sdk.api.model.VKList
import kotlinx.android.synthetic.main.fragment_news_list.*

class NewsListFragment: BaseMvpFragment(), NewsTab, SwipeRefreshLayout.OnRefreshListener {
    override val layoutId: Int
        get() = R.layout.fragment_news_list

    private var adapter : NewsAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = NewsAdapter(Glide.with(this)){ news, liked-> onLikePressed(news,liked)}
        news_list.adapter = adapter
        (activity as MainActivity).supportActionBar?.hide()
        refresh()
        swipe_refresh.setOnRefreshListener(this)
    }

    override fun refresh() {
        feed_loading.visibility = View.VISIBLE
        VKApi.wall().get(VKParameters.from(VKApiConst.ACCESS_TOKEN, Constants.VK_API_ACCESS_TOKEN,
            VKApiConst.COUNT, 15,
            VKApiConst.OWNER_ID, "-96281069",
            VKApiConst.EXTENDED,1,
            VKApiConst.FILTERS, "owner",
            VKApiConst.VERSION, 5.103
        ))
            .executeWithListener(object : VKRequest.VKRequestListener() {
                override fun onComplete(response: VKResponse?) {
                    val resultList = response?.parsedModel as VKList<VKApiPost>
                    adapter?.newsList?.clear()
                    //adapter?.notifyDataSetChanged()
                    val prefs = activity?.getSharedPreferences(Constants.SHAREDPREF, Context.MODE_PRIVATE)
                    val list = Gson().fromJson(prefs?.getString(Constants.LIKED_LIST,""),Constants.NEWS_LIST_TYPE)?:ArrayList<News>()
                    for (x in resultList) {
                        val photoUrl = (x.attachments.firstOrNull { att -> att is VKApiPhoto } as VKApiPhoto?)?.photo_604.toString()
                        val allPhotos = x.attachments.filter { att -> att is VKApiPhoto }.filterNotNull().map { x-> (x as VKApiPhoto).photo_604 }
                        val news = News(x.id,x.text.split("\n")[0],x.text.subSequence(0,if ( x.text.length<120 ){ x.text.length }else{ 120}).toString().plus("..."), x.text,
                            true,photoUrl,allPhotos)
                        news.liked = list.contains(news)
                        adapter?.newsList?.add(news)
                    }
                    adapter?.notifyDataSetChanged()
                    feed_loading.visibility = View.GONE
                    swipe_refresh.isRefreshing = false
                }
                override fun onError(error: VKError?) {
                    feed_loading.visibility = View.GONE
                    swipe_refresh.isRefreshing = false
                    super.onError(error)
                }
            })
    }

    @SuppressLint("ApplySharedPref")
    private fun onLikePressed(news: News, liked: Boolean){
        val prefs = activity?.getSharedPreferences(Constants.SHAREDPREF, Context.MODE_PRIVATE)
        val list = Gson().fromJson(prefs?.getString(Constants.LIKED_LIST,""),Constants.NEWS_LIST_TYPE)?:ArrayList<News>()
        if (liked)
            if (!list.contains(news)){
                list.add(news)
            }
        else
            if (list.contains(news)){
                list.remove(news)
            }
        prefs?.edit()?.putString(Constants.LIKED_LIST,Gson().toJson(list))?.commit()
    }

    override fun getTitle() = "Новости"
    override fun onRefresh() {
        swipe_refresh.isRefreshing = true
        refresh()
    }
}