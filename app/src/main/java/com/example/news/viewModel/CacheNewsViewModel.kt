package com.example.news.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.news.cache.NewsDatabase
import com.example.news.cache.data.BookmarkedNews
import com.example.news.cache.data.CachedNews
import com.example.news.cache.repository.BookmarkedNewsRepository
import com.example.news.cache.repository.CacheNewsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CacheNewsViewModel(application : Application, cacheLimit : Int):AndroidViewModel(application) {
    private val cacheRepository : CacheNewsRepository
    val cachedNews : LiveData<List<CachedNews>>

    private val bookmarkRepository : BookmarkedNewsRepository
    val bookmarkNews : LiveData<List<BookmarkedNews>>

    private val _bookmarkedIds = MutableLiveData<Set<String>>()
    val bookmarkedIds : LiveData<Set<String>> get() = _bookmarkedIds

    init {
        ///recent
//        val newsDao = NewsDatabase.getDatabase(application).cachedNewsDao()
        cacheRepository = CacheNewsRepository(NewsDatabase.getDatabase(application), cacheLimit)
        cachedNews = cacheRepository.getCachedNews()

        //bookmark
        bookmarkRepository = BookmarkedNewsRepository((NewsDatabase.getDatabase(application)))
        bookmarkNews = bookmarkRepository.getBookmarks()

        //observe bookmark changes and update Ids
        bookmarkNews.observeForever{
             bookmarkList ->
            _bookmarkedIds.value = bookmarkList.map {
                it.id
            }.toSet()
        }
    }

    suspend fun isBookmarked(news: BookmarkedNews) : Boolean{
        return withContext(Dispatchers.IO){
            bookmarkRepository.isBookmarked(news.id)
        }
    }

    fun bookmark(news: BookmarkedNews){
        viewModelScope.launch(Dispatchers.IO) {
            bookmarkRepository.bookmark(news)
        }
    }
    fun deleteBookmark(news: BookmarkedNews){
        viewModelScope.launch(Dispatchers.IO) {
            bookmarkRepository.deleteBookmark(news)
        }
    }
    fun cacheNews(news : CachedNews){
        viewModelScope.launch(Dispatchers.IO) {
            cacheRepository.cacheNews(news)
        }
    }



}