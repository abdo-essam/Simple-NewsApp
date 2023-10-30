package com.example.newsapp.helper

import com.example.newsapp.data.model.Article
import com.example.newsapp.util.Resource

class ResourceResultHandler(
    val onLoading: () -> Unit,
    val onSuccess: (List<Article>) -> Unit,
    val onError: (String) -> Unit
) {


    fun handleResult(result: Resource<List<Article>>) {
        when (result) {
            is Resource.Loading -> {
                onLoading()
                return
            }

            is Resource.Success -> {
                result.data?.let { onSuccess(it) }
                return
            }

            is Resource.Error -> {
                onError(result.message.toString())
                return
            }
        }
    }

}