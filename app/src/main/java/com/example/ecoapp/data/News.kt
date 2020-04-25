package com.example.ecoapp.data

data class News(
    var id:Int,
    var topic: String,
    var fullText: String,
    var liked: Boolean = false,
    var photoUrl: String = "",
    var allPhotos: List<String>,
    var attachedNews: ArrayList<News> = arrayListOf()
){
    var desc: String = fullText.subSequence(0,if (fullText.length<120 ){ fullText.length }else{ 120}).toString().plus("...")

    init {
        if (photoUrl.isEmpty())
            photoUrl = allPhotos.firstOrNull().toString()
    }
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