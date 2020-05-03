package com.example.ecoapp.data

import com.google.firebase.firestore.Exclude

data class News(
    var id:Int,
    var topic: String,
    var fullText: String,
    @Exclude
    var liked: Boolean = false,
    @Exclude
    var vkUrl: String = "",
    var allPhotos: List<String>,
    var attachedNews: ArrayList<Int> = arrayListOf(),
    @Exclude
    val attachedNewsCached: ArrayList<News> = arrayListOf()
){
    constructor() : this(0, "", "", false, "", listOf())

    val desc get() = fullText.subSequence(0,if (fullText.length<120 ){ fullText.length }else{ 120}).toString().plus("...")

    val photoUrl get() = if (vkUrl.isEmpty())
        allPhotos.firstOrNull().toString()
    else vkUrl

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + topic.hashCode()
        result = 31 * result + desc.hashCode()
        result = 31 * result + fullText.hashCode()
        return result
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as News

        if (id != other.id) return false
        if (topic != other.topic) return false
        if (desc != other.desc) return false
        if (fullText != other.fullText) return false

        return true
    }
}