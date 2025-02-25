package com.example.news.cache.repository

import androidx.lifecycle.LiveData
import com.example.news.cache.NewsDatabase
import com.example.news.cache.data.CachedNews

class CacheNewsRepository (private val newsDatabase: NewsDatabase,
                           private val cacheLimit : Int){

    private val cachedNewsDao = newsDatabase.cachedNewsDao()

    suspend fun cacheNews(news : CachedNews){
        cachedNewsDao.insert(news)
        cachedNewsDao.deleteOldEntries(cacheLimit)
    }
    fun getCachedNews() : LiveData<List<CachedNews>>{
        return cachedNewsDao.getRecentNews(cacheLimit)
    }

}