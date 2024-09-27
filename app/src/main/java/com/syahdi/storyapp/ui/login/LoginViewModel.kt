package com.syahdi.storyapp.ui.login

import android.util.Log
import androidx.lifecycle.ViewModel
import com.syahdi.storyapp.data.remote.ApiConfig
import com.syahdi.storyapp.data.model.LoginModel
import com.syahdi.storyapp.data.model.SingleLiveEvent
import com.syahdi.storyapp.data.local.preferences.UserPreferences
import com.syahdi.storyapp.data.response.LoginResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel : ViewModel() {

    private val _message = SingleLiveEvent<String>()
    val message: SingleLiveEvent<String> = _message

    companion object {
        private const val TAG = "LoginViewModel"
    }

    fun authenticateUser(email: String, password: String) {
        val loginModel = LoginModel(email, password)
        val client = ApiConfig.authenticationApiService().loginUser(loginModel)
        client.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(
                call: Call<LoginResponse>,
                response: Response<LoginResponse>
            ) {
                if (response.isSuccessful) {
                    _message.value = "success"

                    Log.e(TAG, "isSuccessful: ${response.message()}")
                    Log.e(TAG, "${response.body()}")

                    val token = response.body()?.loginResult?.token

                    if (token != null) {
                        UserPreferences.putString("token", token)
                        Log.d(TAG, "Token saved: $token")
                    } else {
                        Log.e(TAG, "Token not found in response body")
                    }
                } else {
                    _message.value = "failed"
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                _message.value = "failed"
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }
}