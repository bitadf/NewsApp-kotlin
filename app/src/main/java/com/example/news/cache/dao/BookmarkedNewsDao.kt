package com.example.news.cache.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.news.cache.data.BookmarkedNews
@Dao
interface BookmarkedNewsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun bookmark(news : BookmarkedNews)

    @Query("SELECT * FROM bookmarked_news ORDER BY timestamp DESC")
    fun getBookmarks() : LiveData<List<BookmarkedNews>>

    @Delete
    suspend fun deleteBookmark(news : BookmarkedNews)

    @Query("SELECT EXISTS(SELECT 1 FROM bookmarked_news WHERE id = :id LIMIT 1)")
    suspend fun isBookmarked(id : String): Boolean

}