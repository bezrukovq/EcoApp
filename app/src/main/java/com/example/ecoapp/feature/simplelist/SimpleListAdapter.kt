package com.example.ecoapp.feature.simplelist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ecoapp.R
import com.example.ecoapp.data.SimpleListItem
import kotlinx.android.synthetic.main.item_simple_list.view.*

class SimpleListAdapter : RecyclerView.Adapter<SimpleListAdapter.SimpleHolder>(){

    val list = arrayListOf(
        SimpleListItem("Пластик",""),
        SimpleListItem("Углеводород",""),
        SimpleListItem("Картоха",""),
        SimpleListItem("Петушки сладкие","")
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SimpleHolder =
        SimpleHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_simple_list,parent,false))

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: SimpleHolder, position: Int) {
        holder.bind(list[position])
    }

    inner class SimpleHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        fun bind(item: SimpleListItem){
            itemView.item_title.text = item.title
        }
    }

}