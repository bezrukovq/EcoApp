package com.example.ecoapp.data

import com.example.ecoapp.R

data class SimpleListItem(
    val title: String,
    val imgUrl: String = "",
    val imgRes: Int = R.drawable.icon_plastic,
    val news: News? = null
)