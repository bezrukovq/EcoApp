package com.example.ecoapp.data

data class News(
    var id:Int,
    var topic: String,
    var desc: String,
    var fullText: String,
    var liked: Boolean
){
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