package com.example.news.cache

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.news.cache.dao.BookmarkedNewsDao
import com.example.news.cache.dao.CashedNewsDao
import com.example.news.cache.data.BookmarkedNews
import com.example.news.cache.data.CachedNews

@Database(
    entities = [CachedNews::class , BookmarkedNews::class] ,
    version = 4 ,
    exportSchema = false
)
abstract class NewsDatabase : RoomDatabase(){
    abstract fun cachedNewsDao() : CashedNewsDao
    abstract fun bookmarkedNewsDao() : BookmarkedNewsDao

    companion object{
        @Volatile
        private var INSTANCE : NewsDatabase? = null

        fun getDatabase(context: Context):NewsDatabase{
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    NewsDatabase::class.java ,
                    "news_database"
                ).fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }

}

