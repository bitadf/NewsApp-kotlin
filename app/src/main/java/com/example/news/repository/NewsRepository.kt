package com.example.news.repository


import com.example.news.api.RetrofitClient
import com.example.news.data.NewsResponse
import retrofit2.Response


class NewsRepository {
    suspend fun getNews(query : String , date:String) = RetrofitClient.apiService.getNews(query , date)
    suspend fun getCategoryNews(country: String, category: String): Response<NewsResponse> {
        return RetrofitClient.apiService.getCategoryNews(country, category)
    }
    suspend fun getBreakingNews(country: String ) = RetrofitClient.apiService.getBreakingNews(country)
    suspend fun getByDomain(domain: String) = RetrofitClient.apiService.getByDomain(domain)

}