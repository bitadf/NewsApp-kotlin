package com.example.news.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.news.data.Article
import com.example.news.repository.NewsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NewsViewModel : ViewModel() {
    private val repository = NewsRepository()
    val newArticles = MutableLiveData<List<Article>?>()

    val errorMessage = MutableLiveData<String>()


    fun getNews(query : String , date : String){
        viewModelScope.launch (Dispatchers.IO){
            try {
                val response = repository.getNews(query , date)
                if(response.isSuccessful && response.body()?.status == "ok"){
                    newArticles.postValue(response.body()!!.articles)
                }
                else{
                    val errorMessageText = response.body()?.message ?: "Unknown error"
                    errorMessage.postValue("API error: $errorMessageText")
                }
            }
            catch (e : Exception){
                errorMessage.postValue("Network error: ${e.message}")
            }
        }
    }
    fun getCategoryNews(country: String, category: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = repository.getCategoryNews(country, category)
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body?.status == "ok") {
                        // Success: Update articles
                        newArticles.postValue(body.articles ?: emptyList())
                    } else {
                        // Error: Log the raw response and extract the error message
                        Log.d("NewsViewModel", "Error response: ${response.errorBody()?.string()}")
                        val errorMessageText = body?.message ?: "Unknown error"
                        errorMessage.postValue("API error: $errorMessageText")
                    }
                } else {
                    // Handle HTTP errors (e.g., 404, 500)
                    Log.d("NewsViewModel", "HTTP error: ${response.errorBody()?.string()}")
                    errorMessage.postValue("HTTP error: ${response.message()}")
                }
            } catch (e: Exception) {
                // Handle network or other exceptions
                Log.e("NewsViewModel", "CRASH: ${e.stackTraceToString()}")
                errorMessage.postValue("Network error: ${e.message}")
            }
        }
    }
    fun getByDomainNews(domain : String){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = repository.getByDomain(domain)
                if(response.isSuccessful){
                    val body = response.body()
                    if(body?.status =="ok")
                    {
                        newArticles.postValue(body.articles?: emptyList())
                    }
                    else{
                        // Error: Log the raw response and extract the error message
                        Log.d("NewsViewModel", "Error response: ${response.errorBody()?.string()}")
                        val errorMessageText = body?.message ?: "Unknown error"
                        errorMessage.postValue("API error: $errorMessageText")
                    }
                }
                else {
                    // Handle HTTP errors (e.g., 404, 500)
                    Log.d("NewsViewModel", "HTTP error: ${response.errorBody()?.string()}")
                    errorMessage.postValue("HTTP error: ${response.message()}")
                }
            }
            catch (e : Exception){
                // Handle network or other exceptions
                Log.e("NewsViewModel", "CRASH: ${e.stackTraceToString()}")
                errorMessage.postValue("Network error: ${e.message}")
            }
        }
    }
    fun getBreakingNews(country: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = repository.getBreakingNews(country)
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body?.status == "ok") {
                        // Success: Update articles
                        newArticles.postValue(body.articles ?: emptyList())
                    } else {
                        // Error: Log the raw response and extract the error message
                        Log.d("NewsViewModel", "Error response: ${response.errorBody()?.string()}")
                        val errorMessageText = body?.message ?: "Unknown error"
                        errorMessage.postValue("API error: $errorMessageText")
                    }
                } else {
                    // Handle HTTP errors (e.g., 404, 500)
                    Log.d("NewsViewModel", "HTTP error: ${response.errorBody()?.string()}")
                    errorMessage.postValue("HTTP error: ${response.message()}")
                }
            } catch (e: Exception) {
                // Handle network or other exceptions
                Log.e("NewsViewModel", "CRASH: ${e.stackTraceToString()}")
                errorMessage.postValue("Network error: ${e.message}")
            }
        }
    }


}