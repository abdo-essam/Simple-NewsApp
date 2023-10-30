package com.example.newsapp.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsapp.data.model.Article
import com.example.newsapp.data.model.NewsResponse
import com.example.newsapp.helper.ArticlesCache
import com.example.newsapp.repositories.ArticlesRepository
import com.example.newsapp.util.Constants.CATEGORY_ARTICLES
import com.example.newsapp.util.Constants.TOP_HEADLINE_ARTICLES
import com.example.newsapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject


@HiltViewModel
class NewsViewModel @Inject constructor(
    private val articlesRepository: ArticlesRepository,
    private val articlesCache: ArticlesCache,
    private val countryCode: String
) : ViewModel() {
    val topHeadlineNews = articlesRepository.getAllArticles()

    private val _topHeadlineNewsProgress = MutableLiveData<Resource<List<Article>>>()
    val topHeadlineNewsProgress: LiveData<Resource<List<Article>>> = _topHeadlineNewsProgress

    private val _featuredArticle = MutableLiveData<Article>()
    val featuredArticle: LiveData<Article> = _featuredArticle


    private var topHeadlineNewsPage = 1
    private var shouldPagingHeadlineNews = true


    init {
        fetchTopHeadlineNews(countryCode, topHeadlineNewsPage)
        getFeaturedArticle()
    }

    /* This function picks a random article from the local database and considers it as a featured article.
      This is not the ideal way to do this in a real app, but the REST API does not provide a featured article.
      Therefore, I am using this function. However,
      the featuredArticle variable will be null when the app is run for the first time,
      because there are no articles stored in the database.
    */
    private fun getFeaturedArticle() = viewModelScope.launch {
        val featuredArticle = articlesRepository.getRandomArticle()
        Log.d("randomArticle", featuredArticle.toString())
        //If it is not null, it calls the postValue()
        featuredArticle?.let { _featuredArticle.postValue(it) }
    }


    fun fetchTopHeadlineNews(country: String, page: Int = topHeadlineNewsPage) =
        viewModelScope.launch {
            try {
                if (shouldPagingHeadlineNews) {
                    _topHeadlineNewsProgress.postValue(Resource.Loading())
                    val response = articlesRepository.fetchTopHeadlineNews(country, page)
                    handleTopHeadlineNewsResponse(response)
                } else
                    _topHeadlineNewsProgress.postValue(Resource.Error("No Paging"))
            } catch (e: Exception) {
                _topHeadlineNewsProgress.postValue(Resource.Error(e.message.toString()))
            }
        }

    private var oldTopHeadlineArticles = ArrayList<Article>()
    private fun handleTopHeadlineNewsResponse(response: Response<NewsResponse>) {
        if (response.isSuccessful)
            response.body()?.let { newsResponse ->
                val articles = newsResponse.articles

                //Updating Paging statues
                if (newsResponse.status != "ok" || newsResponse.articles.isEmpty()) {
                    shouldPagingHeadlineNews = false
                    _topHeadlineNewsProgress.postValue(Resource.Error("No Paging"))
                    return
                }

                fillFeatureArticleForTheFirstTime(articles)

                oldTopHeadlineArticles = oldTopHeadlineArticles.plus(articles) as ArrayList<Article>
                _topHeadlineNewsProgress.postValue(Resource.Success(oldTopHeadlineArticles))
                topHeadlineNewsPage++
                updateCacheStatues(TOP_HEADLINE_ARTICLES, oldTopHeadlineArticles)
            }
        else
            _topHeadlineNewsProgress.postValue(Resource.Error(response.message()))
    }

    // This function will only be executed once, when we first fetch data,
    // to handle the featuredArticle variable when the database is empty.
    private fun fillFeatureArticleForTheFirstTime(articles: List<Article>) {
        if (_featuredArticle.value == null)
            _featuredArticle.postValue(articles.random())
    }

    private fun updateCacheStatues(
        articlesFlag: String,
        articles: List<Article>,
        category: String = ""
    ) =
        viewModelScope.launch {
            when (articlesFlag) {
                TOP_HEADLINE_ARTICLES -> {
                    articlesCache.updateTopHeadlineArticlesCache(articles)
                }
                CATEGORY_ARTICLES -> {
                    articlesCache.updateCategoryArticlesCache(category, articles)
                }
            }
        }


}