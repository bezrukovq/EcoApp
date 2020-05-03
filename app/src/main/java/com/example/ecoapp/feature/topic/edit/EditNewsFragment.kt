package com.example.ecoapp.feature.topic.edit

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.ecoapp.Constants
import com.example.ecoapp.R
import com.example.ecoapp.data.News
import com.example.ecoapp.feature.main.MainActivity
import com.example.ecoapp.feature.news_tabs.newslist.NewsAdapter
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import com.md.nails.presentation.basemvp.BaseMvpFragment
import kotlinx.android.synthetic.main.fragment_news_topic_edit.*


class EditNewsFragment : BaseMvpFragment() {

    override val layoutId: Int
        get() = R.layout.fragment_news_topic_edit


    private var newsAdapter: NewsAdapter? = null

    private var initedNews: News? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val news = Gson().fromJson(arguments?.getString(Constants.NEWS_KEY), News::class.java)
        if (news.topic.isEmpty())
            FirebaseFirestore.getInstance().collection("preparings").whereEqualTo("id", news.id)
                .get().addOnSuccessListener { res ->
                for (doc in res)
                    setNews(doc.toObject(News::class.java))
            }
                .addOnFailureListener { it ->
                    Log.e("AAAAAAAAA", it.message.toString())
                }
        else
            setNews(news)
    }

    fun setNews(news: News) {
        title.setText(news.topic)
        news_topic_text.setText(news.fullText)
        (activity as MainActivity).supportActionBar?.show()
        val prefs = activity?.getSharedPreferences(Constants.SHAREDPREF, Context.MODE_PRIVATE)
        val list =
            Gson().fromJson(prefs?.getString(Constants.LIKED_LIST, ""), Constants.NEWS_LIST_TYPE)
                ?: ArrayList<News>()
        news.liked = list.contains(news)

        newsAdapter =
            NewsAdapter(Glide.with(this)) { itnews, liked -> onLikePressed(itnews, liked) }
        news_list.adapter = newsAdapter
        if (news.attachedNews.size != news.attachedNewsCached.size) {
            news.attachedNewsCached.clear()
            for (attached in news.attachedNews) {
                FirebaseFirestore.getInstance().collection("preparings")
                    .whereEqualTo("id", attached)
                    .get().addOnSuccessListener { res ->
                        for (doc in res)
                            news.attachedNewsCached.add(doc.toObject(News::class.java))
                        setAttached(news)
                    }
                    .addOnFailureListener { it ->
                        Log.e("AAAAAAAAA", it.message.toString())
                    }
            }
        } else
            setAttached(news)
    }

    fun setAttached(news:News){
        newsAdapter?.newsList = news.attachedNewsCached
        newsAdapter?.notifyDataSetChanged()
        initedNews = news
        setNavLabel()
        fab_done.setOnClickListener {
            FirebaseFirestore.getInstance()
                .collection("preparings")
                .whereEqualTo("id",initedNews?.id)
                .get()
                .addOnSuccessListener {res->
                    for(doc in res)
                        FirebaseFirestore.getInstance().collection("preparings").document(doc.id).update(mapOf(
                            "topic" to title.text.toString(),
                            "fullText" to news_topic_text.text.toString()
                        )).addOnSuccessListener {
                            view?.findNavController()?.navigateUp()
                        }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        setNavLabel()
    }

    fun setNavLabel(){
        (activity as MainActivity).supportActionBar?.title = initedNews?.topic?.apply {
            subSequence(
                0, if (length < 120) {
                    length
                } else {
                    20
                }
            ).toString().plus("...")
        }
    }

    @SuppressLint("ApplySharedPref")
    private fun onLikePressed(news: News, liked: Boolean) {
        val prefs = activity?.getSharedPreferences(Constants.SHAREDPREF, Context.MODE_PRIVATE)
        val list =
            Gson().fromJson(prefs?.getString(Constants.LIKED_LIST, ""), Constants.NEWS_LIST_TYPE)
                ?: ArrayList<News>()
        if (liked) {
            if (!list.contains(news)) {
                list.add(news)
            }
        } else
            if (list.contains(news)) {
                list.remove(news)
            }
        prefs?.edit()?.putString(Constants.LIKED_LIST, Gson().toJson(list))?.commit()
    }
}