package com.syahdi.storyapp.ui.main

import android.content.Context
import android.util.Log
import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.syahdi.storyapp.data.remote.ApiConfig
import com.syahdi.storyapp.data.StoryRepository
import com.syahdi.storyapp.di.Injection
import com.syahdi.storyapp.data.response.ListStoryItem
import com.syahdi.storyapp.data.response.ListStoryResponse
import retrofit2.*

class MainViewModel(storyRepository: StoryRepository) : ViewModel() {

    private val _stories = MutableLiveData<List<ListStoryItem>>()
    val stories: LiveData<List<ListStoryItem>> = _stories

    val storiesPaging: LiveData<PagingData<ListStoryItem>> =
        storyRepository.getStory().cachedIn(viewModelScope)

    companion object{
        private const val TAG = "MainViewModel"
    }

    fun getAllStories(token: String) {
        val client = ApiConfig.getStoryApiService(token).getAllStories()
        client.enqueue(object : Callback<ListStoryResponse> {
            override fun onResponse(
                call: Call<ListStoryResponse>,
                response: Response<ListStoryResponse>
            ) {
                if (response.isSuccessful) {
                    Log.e(TAG, "isSuccessful: ${response.message()}")
                    Log.e(TAG, "${response.body()}")

                    _stories.value = response.body()?.listStory
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<ListStoryResponse>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }
}

class MainViewModelFactory(private val context: Context, authToken: String) : ViewModelProvider.Factory {
    val token = authToken
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(Injection.provideRepository(context, token)) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}