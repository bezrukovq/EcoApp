package com.example.ecoapp.feature.topic

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.example.ecoapp.Constants
import com.example.ecoapp.R
import com.example.ecoapp.data.News
import com.example.ecoapp.feature.main.MainActivity
import com.example.ecoapp.feature.news_tabs.newslist.NewsAdapter
import com.example.ecoapp.utils.ParseHelper
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import com.md.nails.presentation.basemvp.BaseMvpFragment
import com.smarteist.autoimageslider.IndicatorAnimations
import com.smarteist.autoimageslider.SliderAnimations
import com.smarteist.autoimageslider.SliderView
import kotlinx.android.synthetic.main.fragment_news_topic.*
import safety.com.br.android_shake_detector.core.ShakeDetector
import safety.com.br.android_shake_detector.core.ShakeOptions


class NewsFragment : BaseMvpFragment() {

    override val layoutId: Int
        get() = R.layout.fragment_news_topic

    private var shakeDetector: ShakeDetector? = null

    private val options = ShakeOptions()
        .background(false)
        .interval(1000)
        .shakeCount(2)
        .sensibility(2.0f)

    private var newsAdapter: NewsAdapter? = null

    private var initedNews: News? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    fun openEditMode(){
        if (this.isResumed && initedNews?.vkUrl == "" && view?.findNavController()?.currentDestination?.id == R.id.news_topic_fragment)
        view?.findNavController()?.navigate(R.id.actionOpenTopicEdit,Bundle().apply {
            putString(Constants.NEWS_KEY, Gson().toJson(initedNews.apply { this?.topic = "" }))
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        shakeDetector?.stopShakeDetector(activity)
        shakeDetector?.destroy(activity)
    }

    override fun onResume() {
        super.onResume()
        setNavLabel()
        if (shakeDetector == null)
            shakeDetector = ShakeDetector(options).start(activity,
                { openEditMode() })
    }

    private var adapter: SliderAdapterExample? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val news = Gson().fromJson(arguments?.getString(Constants.NEWS_KEY), News::class.java)
        setImageSlider()
        if (news.topic.isEmpty())
           tryLoad(news.id)
        else {
            setNews(news)
            tryLoad(news.id)
        }
    }

    private fun tryLoad(newsId:Int){
        FirebaseFirestore.getInstance().collection("preparings").whereEqualTo("id", newsId)
            .get().addOnSuccessListener { res ->
                for (doc in res)
                    setNews(doc.toObject(News::class.java))
            }
            .addOnFailureListener { it ->
                Log.e("AAAAAAAAA", it.message.toString())
            }
    }

    fun setNews(news: News) {
        title?.text = ParseHelper.parseIDLinks(news.topic)
        news_topic_text?.text = ParseHelper.parseIDLinks(news.fullText)
        (activity as MainActivity).supportActionBar?.show()
        adapter?.renewItems(news.allPhotos)
        if (news.allPhotos.isEmpty())
            imageSlider.visibility = View.GONE
        else
            imageSlider.visibility = View.VISIBLE
        if (news.allPhotos.size > 1)
            imageSlider.startAutoCycle();
        val prefs = activity?.getSharedPreferences(Constants.SHAREDPREF, Context.MODE_PRIVATE)
        val list =
            Gson().fromJson(prefs?.getString(Constants.LIKED_LIST, ""), Constants.NEWS_LIST_TYPE)
                ?: ArrayList<News>()
        news.liked = list.contains(news)
        if (news.liked)
            topic_like?.progress = 1f
        else
            topic_like?.progress = 0f
        fab_like.setOnClickListener {
            if (news.liked)
                topic_like.speed = -1f
            else
                topic_like.speed = 1f
            news.liked = !news.liked
            topic_like.playAnimation()
            onLikePressed(news, news.liked)
        }
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
        attached?.visibility = if (news.attachedNews.isEmpty()) View.GONE else View.VISIBLE
        initedNews = news
        setNavLabel()
        like_view?.visibility = View.VISIBLE
        news_loading?.visibility = View.GONE
    }

    fun setNavLabel(){
        activity?.let {
            (it as MainActivity).supportActionBar?.title = initedNews?.topic?.apply {
                subSequence(
                    0, if (length < 120) {
                        length
                    } else {
                        120
                    }
                ).toString().plus("...")
            }
        }
    }

    private fun setImageSlider(){
        adapter = SliderAdapterExample(activity)
        imageSlider.setSliderAdapter(adapter!!)
        imageSlider.setIndicatorAnimation(IndicatorAnimations.WORM)
        imageSlider.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION)
        imageSlider.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_RIGHT);
        imageSlider.scrollTimeInSec = 4
        imageSlider.indicatorSelectedColor = resources.getColor(R.color.colorAccent)
        imageSlider.indicatorUnselectedColor = resources.getColor(R.color.colorPrimaryDark)
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