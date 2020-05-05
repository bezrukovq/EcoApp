package com.example.ecoapp.feature.topic.edit

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ecoapp.R
import com.example.ecoapp.data.News
import kotlinx.android.synthetic.main.item_simple_list.view.*

class SimpleNewsListAdapter(private var onLikeListener: (News) -> Unit) :
    RecyclerView.Adapter<SimpleNewsListAdapter.SimpleHolder>() {

    var list = arrayListOf<News>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SimpleHolder =
        SimpleHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_simple_list, parent, false)
        )

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: SimpleHolder, position: Int) {
        holder.bind(list[position])
    }

    inner class SimpleHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: News) {
            itemView.item_title.text = item.topic
            itemView.item_icon.visibility = View.GONE
            itemView.setOnClickListener {
               onLikeListener(item)
            }
        }
    }

}