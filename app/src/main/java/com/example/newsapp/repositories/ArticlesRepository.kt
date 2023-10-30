package com.example.newsapp.repositories

import com.example.newsapp.data.database.ArticlesDatabase
import com.example.newsapp.data.model.Article
import com.example.newsapp.data.networking.NewsApi

class ArticlesRepository(
    private val newsApi: NewsApi,
    articlesDatabase: ArticlesDatabase
) {
    private val articlesDao = articlesDatabase.articlesDao

    suspend fun fetchTopHeadlineNews(
        country: String,
        page: Int
    ) = newsApi.fetchTopHeadlinesNews(country, page)

    suspend fun fetchNewsByCategory(
        country: String,
        page: Int,
        category: String
    ) = newsApi.fetchNewsByCategory(country, page, category)

    suspend fun searchNews(
        searchQuery: String
    ) = newsApi.searchNews(searchQuery)


    suspend fun insertArticle(
        article: Article
    ) = articlesDao.upsertArticle(article)

    suspend fun insertListOfArticles(
        articles: List<Article>
    ) = articlesDao.upsertArticles(articles)

    suspend fun deleteArticle(
        article: Article
    ) = articlesDao.deleteArticle(article)

    suspend fun deleteAllArticles() = articlesDao.deleteTopHeadlineArticles()

    suspend fun deleteCategoryArticles(category: String) =
        articlesDao.deleteCategoryArticles(category)

    fun getAllArticles() = articlesDao.getAllArticles()

    suspend fun getArticlesByCategory(
        category: String
    ) = articlesDao.getArticlesByCategory(category)

    suspend fun getRandomArticle(): Article? = articlesDao.getRandomArticle()


}