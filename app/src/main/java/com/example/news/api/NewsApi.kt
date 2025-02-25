package com.example.news.api

import com.example.news.data.NewsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {
    @GET("everything")
    suspend fun getNews(
        @Query("q") query : String ,
        @Query("from") date : String ,
        @Query("sortedBy") sortBy: String = "publishedAt" ,
        @Query("apiKey") apiKey : String = "92cf4c96f2534a0d908fe2bee06b9eff"
    ) : Response<NewsResponse>

    @GET("top-headlines")
    suspend fun getCategoryNews(
        @Query("country") country: String = "us",
        @Query("category") category: String? = null,
        @Query("apiKey") apiKey: String = "92cf4c96f2534a0d908fe2bee06b9eff"
    ): Response<NewsResponse>

    @GET("top-headlines")
    suspend fun getBreakingNews(

        @Query("country")country : String ,
        @Query("apiKey")apiKey: String = "92cf4c96f2534a0d908fe2bee06b9eff"
    ):Response<NewsResponse>
    @GET("everything")
    suspend fun getByDomain(
        @Query("domains")domains: String ,
        @Query("apiKey")apiKey: String = "92cf4c96f2534a0d908fe2bee06b9eff"
    ):Response<NewsResponse>


//https://newsapi.org/v2/everything?domains=wsj.com&apiKey=92cf4c96f2534a0d908fe2bee06b9eff

    //https://newsapi.org/v2/top-headlines?country=us&apiKey=92cf4c96f2534a0d908fe2bee06b9eff
    //https://newsapi.org/v2/everything?q=tesla&from=2025-01-16&sortBy=publishedAt&apiKey=
//    https://newsapi.org/v2/top-headlines?country=us&category=business&apiKey=

}