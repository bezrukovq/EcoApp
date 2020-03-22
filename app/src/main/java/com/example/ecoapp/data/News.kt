package com.example.ecoapp.data

data class News(
    var id:Int,
    var topic: String,
    var desc: String,
    var fullText: String,
    var liked: Boolean
)