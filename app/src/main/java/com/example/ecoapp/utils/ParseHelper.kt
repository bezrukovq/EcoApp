package com.example.ecoapp.utils

import java.util.regex.Pattern

object ParseHelper {
    fun parseIDLinks(textToParse: String): String {
        var text = textToParse
        val regex = "(id|club)\\d+\\|(.+?)\\]"
        val prefix = "vk.com"
        Pattern.compile(regex).matcher(text).apply {
            while (find()) {
                val link = group().split("|")[0]
                val name = group().split("|")[1].split("]")[0]
                val newText = "$name ($prefix/$link)"
                text = text.replace("[${group()}", newText)
            }
        }
        return text
    }
}