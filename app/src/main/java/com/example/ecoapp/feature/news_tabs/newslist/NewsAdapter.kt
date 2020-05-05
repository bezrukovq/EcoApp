package com.example.ecoapp.feature.news_tabs.newslist

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.example.ecoapp.Constants
import com.example.ecoapp.R
import com.example.ecoapp.data.News
import com.example.ecoapp.utils.ParseHelper
import com.google.gson.Gson
import kotlinx.android.synthetic.main.item_news_list.view.*

class NewsAdapter(private val requestManager: RequestManager,private var onLikeListener: (News,Boolean) -> Unit) : RecyclerView.Adapter<NewsAdapter.NewsHolder>(){

    var newsList : ArrayList<News> = arrayListOf<News>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsHolder =
        NewsHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_news_list,parent,false))

    override fun getItemCount(): Int = newsList.size

    override fun onBindViewHolder(holder: NewsHolder, position: Int) {
        holder.bind(newsList[position])
    }

    inner class NewsHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun bind(news: News){
            val prefs = itemView.context?.getSharedPreferences(Constants.SHAREDPREF, Context.MODE_PRIVATE)
            val list = Gson().fromJson(prefs?.getString(Constants.LIKED_LIST,""),Constants.NEWS_LIST_TYPE)?:ArrayList<News>()
            news.liked = list.contains(news)
            itemView.setOnClickListener {
                itemView.findNavController().navigate(R.id.actionOpenTopic, Bundle().apply {
                    putString(Constants.NEWS_KEY,Gson().toJson(news))
                })
            }
            itemView.topic_title.text = ParseHelper.parseIDLinks(news.topic)
            itemView.topic_desc.text = ParseHelper.parseIDLinks(news.desc)
            requestManager.load(news.photoUrl).into(itemView.topic_image)
           // itemView.topic_like.speed = 100000f
            if (news.liked)
                itemView.topic_like.progress = 1f
            else
                itemView.topic_like.progress = 0f
/*
                itemView.topic_like.reverseAnimationSpeed()
*/
           // itemView.topic_like.playAnimation()
            itemView.topic_like.setOnClickListener {
                if (news.liked)
                    itemView.topic_like.speed = -1f
                else
                    itemView.topic_like.speed = 1f
                news.liked = !news.liked
                itemView.topic_like.playAnimation()
                onLikeListener(news,news.liked)
            }
        }
    }
}