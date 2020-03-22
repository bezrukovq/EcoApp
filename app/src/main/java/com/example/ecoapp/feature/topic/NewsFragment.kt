package com.example.ecoapp.feature.topic

import android.os.Bundle
import android.os.PatternMatcher
import android.util.Log
import android.view.View
import androidx.core.text.isDigitsOnly
import com.example.ecoapp.Constants
import com.example.ecoapp.feature.main.MainActivity
import com.example.ecoapp.R
import com.example.ecoapp.data.News
import com.google.gson.Gson
import com.md.nails.presentation.basemvp.BaseMvpFragment
import kotlinx.android.synthetic.main.fragment_news_topic.*
import java.util.regex.Pattern

class NewsFragment: BaseMvpFragment() {
    override val layoutId: Int
        get() = R.layout.fragment_news_topic

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val news = Gson().fromJson(arguments?.getString(Constants.NEWS_KEY),News::class.java)
        news_topic_text.text = news.fullText
        var text = news.fullText
        val regex = "(id|club)\\d+\\|(.+?)\\]"
        val prefix = "vk.com"
        Pattern.compile(regex).matcher(text).apply {
            while (find()) {
                val link = group().split("|")[0]
                val name = group().split("|")[1].split("]")[0]
                val newText = "$name ($prefix/$link)"
                text = text.replace("[${group()}", newText)
            }
        }
        /*val str = text.split("[id")
        for( x in str){
            for(y in x.split("[cl")){
                for (link in y.split("]")){
                    if (link.split("|")[0].isDigitsOnly()){
                        val userTextToReplace = "\\[id".plus(link.split("|")[0]).plus("\\|").plus(link.split("|")[1]).plus("\\]")
                        val userLink = "vk.com/".plus("id".plus(link.split("|")[0]))
                        val userNewTextWithLink = link.split("|")[1].plus(" (").plus(userLink).plus(")")
                        text = text.replaceFirst(userTextToReplace.toRegex(), userNewTextWithLink)
                    } else if ((link.split("|")[0].split("ub")[0].isDigitsOnly())){
                        val clubTextToReplace = "\\[cl".plus(link.split("|")[0]).plus("\\|").plus(link.split("|")[1]).plus("\\]")
                        val clubLink = "vk.com/".plus("cl".plus(link.split("|")[0]))
                        val clubNewTextWithLink = link.split("|")[1].plus(" (").plus(clubLink).plus(")")
                        text = text.replaceFirst(clubTextToReplace.toRegex(),clubNewTextWithLink)
                    }
                }
            }
        }*/
        news_topic_text.text = text
        (activity as MainActivity).supportActionBar?.show()
            //  (activity as MainActivity).supportActionBar?.title = news.topic
    }
}