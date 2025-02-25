package com.example.news.data

import com.google.gson.annotations.SerializedName

data class NewsResponse(
    @SerializedName("status") val status : String,
    @SerializedName("totalResults") val totalResults : String? = null  ,
    @SerializedName("articles") val articles : List<Article>? = null ,

    @SerializedName("code") val code: String? = null, // Nullable for success responses
    @SerializedName("message") val message: String? = null // Nullable for success responses
)
