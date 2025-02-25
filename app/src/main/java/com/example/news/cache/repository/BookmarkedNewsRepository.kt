package com.example.news.cache.repository

import androidx.lifecycle.LiveData
import com.example.news.cache.NewsDatabase
import com.example.news.cache.data.BookmarkedNews

class BookmarkedNewsRepository(private val newsDatabase: NewsDatabase) {

    private val bookmarkedNewsDao = newsDatabase.bookmarkedNewsDao()

    suspend fun bookmark(news : BookmarkedNews){
        bookmarkedNewsDao.bookmark(news)
    }
    fun getBookmarks() : LiveData<List<BookmarkedNews>>{
       return bookmarkedNewsDao.getBookmarks()
    }
    suspend fun deleteBookmark(news: BookmarkedNews){
        bookmarkedNewsDao.deleteBookmark(news)
    }
    suspend fun isBookmarked(id : String) :Boolean{
        return bookmarkedNewsDao.isBookmarked(id)
    }
}