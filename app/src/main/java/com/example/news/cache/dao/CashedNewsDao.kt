package com.example.news.cache.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.example.news.cache.data.CachedNews

@Dao
interface CashedNewsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(news : CachedNews)

    @androidx.room.Query("SELECT * FROM cached_news ORDER BY timestamp DESC LIMIT :limit")
    fun getRecentNews(limit: Int): LiveData<List<CachedNews>>

    @androidx.room.Query("DELETE FROM cached_news WHERE timestamp NOT IN (SELECT timestamp FROM cached_news ORDER BY timestamp DESC LIMIT :limit)")
    suspend fun deleteOldEntries(limit: Int)
}
