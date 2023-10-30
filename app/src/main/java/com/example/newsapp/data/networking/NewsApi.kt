package com.example.newsapp.data.networking

import androidx.room.Dao
import com.example.newsapp.data.model.NewsResponse
import com.example.newsapp.util.APIKEY.API_KEY
import com.example.newsapp.util.Constants.SORT_BY_POPULARITY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

@Dao
interface NewsApi {

    //https://newsapi.org/v2/top-headlines?country=us&page=1&apiKey=API_KEY
    @GET("v2/top-headlines")
    suspend fun fetchTopHeadlinesNews(
        @Query("country") country:String,
        @Query("page") page:Int = 1,
        @Query("apiKey") apiKey:String = API_KEY
    ) : Response<NewsResponse>

    //https://newsapi.org/v2/top-headlines?country=de&page=1&category=business&apiKey=API_KEY
    @GET("v2/top-headlines")
    suspend fun fetchNewsByCategory (
        @Query("country") country:String,
        @Query("page") page:Int = 1,
        @Query("category") category:String,
        @Query("apiKey") apiKey:String = API_KEY
    ):Response<NewsResponse>

    //https://newsapi.org/v2/everything?q=Apple&sortBy=popularity&apiKey=API_KEY
    @GET("v2/everything")
    suspend fun searchNews(
        @Query("q") searchQuery:String,
        @Query("sortBy") sortBy:String = SORT_BY_POPULARITY,
        @Query("apiKey") apiKey:String = API_KEY
    ):Response<NewsResponse>
}