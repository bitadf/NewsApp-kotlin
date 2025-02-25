package com.example.news.cache.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bookmarked_news")
data class BookmarkedNews(
    @PrimaryKey val id : String ,
    val title : String ,
    val description : String?,
    val content : String ? ,
    val publishedAt : String ,
    val source : String ,
    val image : String? ,
    val category : String ,
    val author : String?,
    @ColumnInfo(name = "timestamp")val timestamp : Long = System.currentTimeMillis()
)
