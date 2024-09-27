package com.syahdi.storyapp.ui.signup

import android.util.Log
import androidx.lifecycle.ViewModel
import com.syahdi.storyapp.data.remote.ApiConfig
import com.syahdi.storyapp.data.model.RegisterModel
import com.syahdi.storyapp.data.response.RegisterResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignUpViewModel : ViewModel() {

    companion object {
        private const val TAG = "SignUpViewModel"
    }

    fun saveUser(name: String, email: String, password: String) {
        val registerModel = RegisterModel(name, email, password)
        val client = ApiConfig.authenticationApiService().registerUser(registerModel)
        client.enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(
                call: Call<RegisterResponse>,
                response: Response<RegisterResponse>
            ) {
                if (response.isSuccessful) {
                    Log.e(TAG, "isSuccessful: ${response.message()}")
                    Log.e(TAG, "${response.body()}")
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }
}