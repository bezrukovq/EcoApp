package com.example.ecoapp.feature.topic.edit

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.addTextChangedListener
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.ecoapp.Constants
import com.example.ecoapp.EcoApp
import com.example.ecoapp.R
import com.example.ecoapp.data.News
import com.example.ecoapp.feature.main.MainActivity
import com.example.ecoapp.feature.news_tabs.newslist.NewsAdapter
import com.google.android.gms.tasks.OnFailureListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.gson.Gson
import com.md.nails.presentation.basemvp.BaseMvpFragment
import kotlinx.android.synthetic.main.fragment_news_topic_edit.*
import okhttp3.internal.notify
import java.lang.reflect.Array


class EditNewsFragment : BaseMvpFragment() {

    override val layoutId: Int
        get() = R.layout.fragment_news_topic_edit


    private var newsAdapter: SimpleNewsListAdapter? = null

    private var suggestAdapter: SimpleNewsListAdapter? = null

    private var initedNews: News? = null

    private var suggestedList = arrayListOf<News>()

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
        setListeners()
        var imagesStr = ""
        for (x in news.allPhotos)
            imagesStr = imagesStr.plus(x).plus("\n")
        imagesList?.setText(imagesStr)
        (activity as MainActivity).supportActionBar?.show()
        val prefs = activity?.getSharedPreferences(Constants.SHAREDPREF, Context.MODE_PRIVATE)
        val list =
            Gson().fromJson(prefs?.getString(Constants.LIKED_LIST, ""), Constants.NEWS_LIST_TYPE)
                ?: ArrayList<News>()
        news.liked = list.contains(news)

        newsAdapter = SimpleNewsListAdapter{ tappedNews-> onDeleteClick(tappedNews) }
        news_list.adapter = newsAdapter
        suggestAdapter = SimpleNewsListAdapter{ tappedNews-> onAddClick(tappedNews) }
        suggestion_list.adapter = suggestAdapter
        FirebaseFirestore.getInstance().collection("preparings").orderBy("id",Query.Direction.ASCENDING)
            .get().addOnSuccessListener { res ->
                for (doc in res)
                    suggestAdapter?.list?.add(doc.toObject(News::class.java))
                suggestAdapter?.notifyDataSetChanged()
                suggestAdapter?.let { suggestedList = it.list }
            }
            .addOnFailureListener(onFailuleListener)
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
                    .addOnFailureListener(onFailuleListener)
            }

        } else
            setAttached(news)
    }

    fun setAttached(news: News) {
        newsAdapter?.list = news.attachedNewsCached
        newsAdapter?.notifyDataSetChanged()
        initedNews = news
        setNavLabel()
    }

    fun setListeners(){
        setCodeListener()
        create_new.setOnClickListener {
            FirebaseFirestore.getInstance().collection("preparings").orderBy("id",Query.Direction.ASCENDING).limit(1)
                .get().addOnSuccessListener {res->
                    for (doc in res){
                        val newNews = News(doc.toObject(News::class.java).id-1,"",fullText =  "", allPhotos = listOf("",""))
                        FirebaseFirestore.getInstance().collection("preparings").add(newNews).addOnSuccessListener {
                            newsAdapter?.list?.add(newNews)
                            newsAdapter?.notifyDataSetChanged()
                            saveChanges({
                                findNavController().navigate(R.id.actionOpenTopicEdit, Bundle().apply {
                                    putString(Constants.NEWS_KEY, Gson().toJson(newNews)) })
                            },true)
                        }
                    }
                }
        }
        add_fromExisting.setOnClickListener {
            val listToShow = arrayListOf<News>()
            listToShow.addAll(suggestedList)
            suggestAdapter?.list = arrayListOf<News>().apply {addAll(listToShow.filter { news-> newsAdapter?.list?.contains(news) != true })}
            suggestAdapter?.notifyDataSetChanged()
            if (suggestAdapter?.list?.isNotEmpty() == true)
                suggestion_list.visibility = View.VISIBLE
            else
                Toast.makeText(activity,"Нет доступных постов к прикреплению",Toast.LENGTH_LONG).show()
        }
        fab_done.setOnClickListener {
            saveChanges()
        }
    }
    val onFailuleListener = OnFailureListener {
        Toast.makeText(activity, it.message.toString(), Toast.LENGTH_LONG).show()
        news_loading?.visibility = View.GONE
    }

    fun saveChanges(onSavedListener: () -> Unit = {},listening: Boolean = false){
        news_loading?.visibility = View.VISIBLE
        FirebaseFirestore.getInstance()
            .collection("preparings")
            .whereEqualTo("id", initedNews?.id)
            .get()
            .addOnSuccessListener { res ->
                if (this.isResumed)
                    for (doc in res)
                        FirebaseFirestore.getInstance().collection("preparings")
                            .document(doc.id).update(
                                mapOf(
                                    "topic" to title?.text.toString(),
                                    "fullText" to news_topic_text?.text.toString(),
                                    "allPhotos" to imagesList.text.toString().split("\n").filter { x-> x.isNotEmpty() },
                                    "attachedNews" to newsAdapter?.list?.map { news -> news.id }?.distinct()
                                )
                            ).addOnSuccessListener {
                                if (!listening)
                                    view?.findNavController()?.navigateUp()
                                else {
                                    onSavedListener()
                                }
                            }.addOnFailureListener(onFailuleListener)

            }
            .addOnFailureListener(onFailuleListener)
    }
    private fun setCodeListener(){
        code?.addTextChangedListener{
            if (it.toString().equals("258013")){
                code_layer.visibility = View.GONE
                EcoApp.isEditor = true
                title?.requestFocus()
                code.setText("")
            }
        }
    }

    private fun hideKeyBoard(){
        val inputMethodManager: InputMethodManager =
            activity?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(
            activity?.currentFocus?.windowToken,
            InputMethodManager.HIDE_NOT_ALWAYS
        )
    }

    override fun onResume() {
        super.onResume()
        setNavLabel()
        if (EcoApp.isEditor)
            code_layer.visibility = View.GONE
    }

    fun setNavLabel() {
        (activity as MainActivity).supportActionBar?.title =
            "[Редактирование] ".plus(initedNews?.topic?.apply {
                subSequence(
                    0, if (length < 80) {
                        length
                    } else {
                        79
                    }
                ).toString().plus("...")
            }
            )
    }

    fun onAddClick(news: News){
        newsAdapter?.list?.add(news)
        newsAdapter?.notifyDataSetChanged()
        suggestion_list.visibility = View.GONE
    }

    fun onDeleteClick(news: News){
        activity?.let {
            AlertDialog.Builder(it)
                .setMessage("Удалить \"${news.topic}\" из прикрепленных к \"${initedNews?.topic}\"?")
                .setTitle("").setPositiveButton(
                    "Удалить"
                ) { dialog, which ->
                    newsAdapter?.list?.remove(news)
                    newsAdapter?.notifyDataSetChanged()
                }.setOnDismissListener {}
                .setNegativeButton("Отмена") { dialog, which -> }
                .create().apply {
                    window?.setBackgroundDrawableResource(R.drawable.dialog_background_shape)
                    show()
                }
        }
    }
}