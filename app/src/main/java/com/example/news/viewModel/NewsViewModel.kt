package com.example.news.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.news.data.Article
import com.example.news.repository.NewsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch

class NewsViewModel : ViewModel() {
    private val repository = NewsRepository()
    val newArticles = MutableLiveData<List<Article>?>()
    private var favoriteCategories = MutableLiveData<List<String>>().apply {
        value = listOf("Business"  , "Science" , "Sports")
    }

    val errorMessage = MutableLiveData<String>()

    val recommendedArticles = MutableLiveData<List<Article>?>()
    fun fetchRecommendedNews(){

        viewModelScope.launch (Dispatchers.IO){
            // Wait for favorite categories to be loaded
//            favoriteCategories.value ?: fetchFavorite()
            val categories = favoriteCategories.value ?: emptyList()

            if(categories.isEmpty())return@launch
            val deferredResults = categories.map { category->
                async {
                    try {
                        val response = repository.getCategoryNews("us" , category)
                        if(response.isSuccessful){
                            response.body()?.articles?.take(2) ?: emptyList()
                        }
                        else{
                            emptyList()
                        }
                    }
                    catch (e : Exception){
                        emptyList()
                    }
                }
            }
            val results = deferredResults.awaitAll()
            val allArticles = results.flatten().shuffled()
            recommendedArticles.postValue(allArticles)

        }
    }


    fun fetchFavorite(){
        viewModelScope.launch(Dispatchers.IO) {
            val test = listOf("Business" , "Science" , "Sports")
                favoriteCategories.postValue(test)

        }
    }
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