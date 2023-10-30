package com.example.newsapp.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.RequestManager
import com.example.newsapp.adapters.ArticlesAdapter
import com.example.newsapp.data.model.Article
import com.example.newsapp.databinding.FragmentTopHeadlineNewsBinding
import com.example.newsapp.helper.ResourceResultHandler
import com.example.newsapp.helper.VerticalRecyclerViewDecoration
import com.example.newsapp.viewModels.NewsViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class TopHeadlineNewsFragment : Fragment() {

    val TAG = "TopHeadlineNewsFragment"

    @Inject
    lateinit var glide: RequestManager


    private val viewModel by activityViewModels<NewsViewModel>()

    private lateinit var binding: FragmentTopHeadlineNewsBinding
    private lateinit var topHeadlinesProgressHandler: ResourceResultHandler
    private lateinit var articlesAdapter: ArticlesAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTopHeadlineNewsBinding.inflate(inflater, container, false)
        return binding.root
    }


    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupArticlesRecyclerview()

        topHeadlinesProgressHandler = ResourceResultHandler(
            onLoading = {
                Log.d(TAG, "TopHeadLines:Loading...")
                showLoading()
            },

            onSuccess = { articlesList ->
                Log.d(TAG, "TopHeadLines:Success :) ${articlesList.size}")
                hideLoading()
                hidePagingLoading()
            },

            onError = { error ->
                Log.e(TAG, "TopHeadLines:Error :( $error")
                hideLoading()
                hidePagingLoading()
            }
        )
        viewModel.topHeadlineNewsProgress.observe(viewLifecycleOwner) { result ->
            topHeadlinesProgressHandler.handleResult(result)
        }
        viewModel.topHeadlineNews.observe(viewLifecycleOwner) { articles ->
            articlesAdapter.differ.submitList(articles)
        }

        var featuredArticle: Article? = null
        viewModel.featuredArticle.observe(viewLifecycleOwner) { featuredArticleResult ->
            setupFeaturedArticle(featuredArticleResult)
            featuredArticle = featuredArticleResult

        }

    }
        private fun hidePagingLoading() {
            binding.progressbarHeadlineNewsPaging.visibility = View.GONE
            binding.refreshTopHeadlineNews.isRefreshing = false
        }

        private fun setupFeaturedArticle(featuredArticle: Article) {
            glide.load(featuredArticle.urlToImage).into(binding.imgFeaturedArticle)
            binding.tvFeaturedArticleTitle.text = featuredArticle.title
        }

        private fun hideLoading() {
            binding.progressbar.visibility = View.INVISIBLE
            binding.refreshTopHeadlineNews.isRefreshing = false
        }

        private fun showLoading() {
            binding.progressbar.visibility = View.VISIBLE
        }

        private fun setupArticlesRecyclerview() {
            articlesAdapter = ArticlesAdapter()
            binding.rvTopHeadlines.apply {
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                adapter = articlesAdapter
                addItemDecoration(VerticalRecyclerViewDecoration(60))
            }
        }

    }