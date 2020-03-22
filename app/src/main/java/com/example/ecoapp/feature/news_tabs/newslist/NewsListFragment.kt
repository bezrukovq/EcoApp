package com.example.ecoapp.feature.news_tabs.newslist

import android.content.Context
import android.os.Bundle
import android.view.View
import com.example.ecoapp.Constants
import com.example.ecoapp.feature.main.MainActivity
import com.example.ecoapp.R
import com.example.ecoapp.data.News
import com.example.ecoapp.feature.news_tabs.NewsTab
import com.md.nails.presentation.basemvp.BaseMvpFragment
import com.vk.sdk.api.*
import com.vk.sdk.api.model.VKApiPost
import com.vk.sdk.api.model.VKList
import kotlinx.android.synthetic.main.fragment_news_list.*

class NewsListFragment: BaseMvpFragment(), NewsTab {
    override val layoutId: Int
        get() = R.layout.fragment_news_list

    val adapter = NewsAdapter{id,liked-> onLikePressed(id,liked)}

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        news_list.adapter = adapter
        (activity as MainActivity).supportActionBar?.hide()
        VKApi.wall().get(VKParameters.from(VKApiConst.ACCESS_TOKEN, Constants.VK_API_ACCESS_TOKEN,
            VKApiConst.COUNT, 10,
            VKApiConst.OWNER_ID, "-96281069",
            VKApiConst.EXTENDED,1,
            VKApiConst.FILTERS, "owner",
            VKApiConst.VERSION, 5.103
            ))
            .executeWithListener(object : VKRequest.VKRequestListener() {
                override fun onComplete(response: VKResponse?) {
                    val resultList = response?.parsedModel as VKList<VKApiPost>
                    adapter.newsList.clear()
                    for (x in resultList)
                        adapter.newsList.add(News(x.id,x.text.split("\n")[0],x.text.subSequence(0,if ( x.text.length<120 ){ x.text.length }else{ 120}).toString().plus("..."), x.text,
                            activity?.getSharedPreferences(Constants.SHAREDPREF,Context.MODE_PRIVATE)?.getBoolean(x.id.toString(),false)?:false))
                    adapter.notifyDataSetChanged()
                }
                override fun onError(error: VKError?) { super.onError(error)
                }
            })
    }

    private fun onLikePressed(id: Int, liked: Boolean){
        val prefs = activity?.getSharedPreferences(Constants.SHAREDPREF, Context.MODE_PRIVATE)?.edit()
        if (liked)
            prefs?.putBoolean(id.toString(),liked)
        else
            prefs?.remove(id.toString())
        prefs?.commit()
    }

    override fun getTitle() = "Новости"
}