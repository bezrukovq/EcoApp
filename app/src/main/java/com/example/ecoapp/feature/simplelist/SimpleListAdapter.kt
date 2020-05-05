package com.example.ecoapp.feature.simplelist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.example.ecoapp.Constants
import com.example.ecoapp.R
import com.example.ecoapp.data.News
import com.example.ecoapp.data.SimpleListItem
import com.google.gson.Gson
import kotlinx.android.synthetic.main.item_simple_list.view.*

class SimpleListAdapter(private val requestManager: RequestManager? = null) :
    RecyclerView.Adapter<SimpleListAdapter.SimpleHolder>() {

    val news =  News(-1,"",fullText =  "",
        allPhotos = listOf("",""))

    val list = arrayListOf(
        SimpleListItem("Пластик", imgRes = R.drawable.icon_plastic, news = news),
        SimpleListItem("Бумага", imgRes =  R.drawable.icon_paper),
        SimpleListItem("Стекло", imgRes = R.drawable.icon_glass),
        SimpleListItem("Металл", imgRes = R.drawable.icon_metal),
        SimpleListItem("Батарейки", imgRes = R.drawable.icon_battery)
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SimpleHolder =
        SimpleHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_simple_list, parent, false)
        )

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: SimpleHolder, position: Int) {
        holder.bind(list[position])
    }

    inner class SimpleHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: SimpleListItem) {
            itemView.item_title.text = item.title
            requestManager?.load(item.imgUrl)?.into(itemView.item_icon)
                ?: itemView.item_icon.setImageResource(item.imgRes)
            if (item.news != null)
            itemView.setOnClickListener {
                itemView.findNavController().navigate(R.id.actionOpenTopic, Bundle().apply {
                    putString(Constants.NEWS_KEY, Gson().toJson(item.news))
                })
            }
        }
    }

}