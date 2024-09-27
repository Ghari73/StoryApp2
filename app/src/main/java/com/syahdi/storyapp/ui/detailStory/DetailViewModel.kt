package com.syahdi.storyapp.ui.detailStory

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.syahdi.storyapp.data.remote.ApiConfig
import com.syahdi.storyapp.data.response.DetailStoryResponse
import com.syahdi.storyapp.data.response.Story
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel : ViewModel() {

    private val _story = MutableLiveData<Story>()
    val story: LiveData<Story> = _story

    companion object{
        private const val TAG = "DetailViewModel"
    }

    fun getStory(token : String, id: String) {
        val client = ApiConfig.getStoryApiService(token).getDetailStory(id)
        client.enqueue(object : Callback<DetailStoryResponse> {
            override fun onResponse(
                call: Call<DetailStoryResponse>,
                response: Response<DetailStoryResponse>
            ) {
                if (response.isSuccessful) {
                    Log.e(TAG, "isSuccessful: ${response.message()}")
                    Log.e(TAG, "${response.body()}")

                    _story.value = response.body()?.story
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<DetailStoryResponse>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })

    }
}