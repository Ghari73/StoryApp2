package com.syahdi.storyapp.di

import android.content.Context
import com.syahdi.storyapp.data.remote.ApiConfig
import com.syahdi.storyapp.data.StoryRepository
import com.syahdi.storyapp.data.local.room.StoryDatabase

object Injection {
    fun provideRepository(context: Context, token: String): StoryRepository {
        val database = StoryDatabase.getDatabase(context)
        val apiService = ApiConfig.getStoryApiService(token) // harus ada token di sini
        return StoryRepository(database, apiService)
    }
}