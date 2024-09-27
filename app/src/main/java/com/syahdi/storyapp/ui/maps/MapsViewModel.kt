package com.syahdi.storyapp.ui.maps

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.syahdi.storyapp.data.remote.ApiConfig
import com.syahdi.storyapp.data.response.ListStoryItem
import com.syahdi.storyapp.data.response.ListStoryResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MapsViewModel : ViewModel() {
    private val _stories = MutableLiveData<List<ListStoryItem>>()
    val stories: LiveData<List<ListStoryItem>> = _stories

    companion object{
        private const val TAG = "MapsViewModel"
    }

    fun getAllStoriesWithLocation(token: String) {
        val client = ApiConfig.getStoryApiService(token).getAllStoriesWithLocation()
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