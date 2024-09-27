package com.syahdi.storyapp.data.remote

import com.syahdi.storyapp.data.response.AddStoryResponse
import com.syahdi.storyapp.data.response.DetailStoryResponse
import com.syahdi.storyapp.data.response.ListStoryResponse
import com.syahdi.storyapp.data.response.LoginResponse
import com.syahdi.storyapp.data.response.RegisterResponse
import com.syahdi.storyapp.data.model.LoginModel
import com.syahdi.storyapp.data.model.RegisterModel
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @POST("register")
    fun registerUser(
        @Body registerModel: RegisterModel
    ): Call<RegisterResponse>

    @POST("login")
    fun loginUser(
        @Body loginModel: LoginModel
    ): Call<LoginResponse>

    @GET("stories")
    fun getAllStories(
    ): Call<ListStoryResponse>

    @GET("stories")
    fun getAllStoriesPaging(
        @Query("page") page: Int,
        @Query("size") size: Int
    ): Call<ListStoryResponse>

    @GET("stories?location=1")
    fun getAllStoriesWithLocation(
    ): Call<ListStoryResponse>

    @GET("stories/{id}")
    fun getDetailStory(
        @Path("id") id: String
    ): Call<DetailStoryResponse>

    @Multipart
    @POST("stories")
    fun createNewStory(
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
    ): Call<AddStoryResponse>
}