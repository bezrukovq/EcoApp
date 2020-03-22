package com.example.ecoapp.feature.news_tabs.newslist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.ecoapp.Constants
import com.example.ecoapp.R
import com.example.ecoapp.data.News
import com.google.gson.Gson
import kotlinx.android.synthetic.main.item_news_list.view.*

class NewsAdapter(private var onLikeListener: (Int,Boolean) -> Unit) : RecyclerView.Adapter<NewsAdapter.NewsHolder>(){

    var newsList = arrayListOf(
        News(1,"AAAAAA","Речь"," о Мелеузовском сахарном заводе в Башкирии (входит в группу «Продимекс»), Нурлатском сахарном заводе в Татарстане (входит в местную группу «Агро-инвест») и Товарковском сахарном заводе (принадлежит структуре Россельхозбанка) в Тульской области.",true),
        News(2,"AAAAAA","Речь"," о Мелеузовском сахарном заводе в Башкирии (входит в группу «Продимекс»), Нурлатском сахарном заводе в Татарстане (входит в местную группу «Агро-инвест») и Товарковском сахарном заводе (принадлежит структуре Россельхозбанка) в Тульской области.",false),
        News(3,"AAAAAA","Речь"," о Мелеузовском сахарном заводе в Башкирии (входит в группу «Продимекс»), Нурлатском сахарном заводе в Татарстане (входит в местную группу «Агро-инвест») и Товарковском сахарном заводе (принадлежит структуре Россельхозбанка) в Тульской области.",false),
        News(4,"AAAAAA","Речь"," о Мелеузовском сахарном заводе в Башкирии (входит в группу «Продимекс»), Нурлатском сахарном заводе в Татарстане (входит в местную группу «Агро-инвест») и Товарковском сахарном заводе (принадлежит структуре Россельхозбанка) в Тульской области.",true),
        News(5,"AAAAAA","Речь"," о Мелеузовском сахарном заводе в Башкирии (входит в группу «Продимекс»), Нурлатском сахарном заводе в Татарстане (входит в местную группу «Агро-инвест») и Товарковском сахарном заводе (принадлежит структуре Россельхозбанка) в Тульской области.",false),
        News(6,"AAAAAA","Речь"," о Мелеузовском сахарном заводе в Башкирии (входит в группу «Продимекс»), Нурлатском сахарном заводе в Татарстане (входит в местную группу «Агро-инвест») и Товарковском сахарном заводе (принадлежит структуре Россельхозбанка) в Тульской области.",false),
        News(7,"AAAAAA","Речь"," о Мелеузовском сахарном заводе в Башкирии (входит в группу «Продимекс»), Нурлатском сахарном заводе в Татарстане (входит в местную группу «Агро-инвест») и Товарковском сахарном заводе (принадлежит структуре Россельхозбанка) в Тульской области.",true),
        News(8,"AAAAAA","Речь"," о Мелеузовском сахарном заводе в Башкирии (входит в группу «Продимекс»), Нурлатском сахарном заводе в Татарстане (входит в местную группу «Агро-инвест») и Товарковском сахарном заводе (принадлежит структуре Россельхозбанка) в Тульской области.",false)
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsHolder =
        NewsHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_news_list,parent,false))

    override fun getItemCount(): Int = newsList.size

    override fun onBindViewHolder(holder: NewsHolder, position: Int) {
        holder.bind(newsList[position])
    }

    inner class NewsHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun bind(news: News){
            itemView.setOnClickListener {
                itemView.findNavController().navigate(R.id.actionOpenTopic, Bundle().apply {
                    putString(Constants.NEWS_KEY,Gson().toJson(news))
                })
            }
            itemView.topic_title.text = news.topic
            itemView.topic_desc.text = news.desc
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
                onLikeListener(news.id,news.liked)
            }
        }
    }
}