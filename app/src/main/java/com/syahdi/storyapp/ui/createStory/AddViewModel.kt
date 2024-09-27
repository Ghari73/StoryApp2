package com.syahdi.storyapp.ui.createStory

import android.util.Log
import androidx.lifecycle.ViewModel
import com.syahdi.storyapp.data.remote.ApiConfig
import com.syahdi.storyapp.data.model.SingleLiveEvent
import com.syahdi.storyapp.data.response.AddStoryResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.*

class AddViewModel : ViewModel() {

    private val _message = SingleLiveEvent<String>()
    val message: SingleLiveEvent<String> = _message

    companion object {
        private const val TAG = "AddViewModel"
    }

    fun addStory(imageMultipart: MultipartBody.Part, description: RequestBody, token: String) {
        val client = ApiConfig.addStoryApiService(token).createNewStory(imageMultipart, description)
        client.enqueue(object : Callback<AddStoryResponse> {
            override fun onResponse(
                call: Call<AddStoryResponse>,
                response: Response<AddStoryResponse>
            ) {
                if (response.isSuccessful) {
                    Log.e(TAG, "isSuccessful: ${response.message()}")
                    Log.e(TAG, "${response.body()}")

                    _message.value = "success"
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")

                    _message.value = "failed"
                }
            }

            override fun onFailure(call: Call<AddStoryResponse>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message.toString()}")
                _message.value = "failed"
            }
        })
    }
}