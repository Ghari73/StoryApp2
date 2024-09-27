package com.syahdi.storyapp.data

import androidx.lifecycle.LiveData
import androidx.paging.*
import com.syahdi.storyapp.data.remote.ApiService
import com.syahdi.storyapp.data.local.room.StoryDatabase
import com.syahdi.storyapp.data.remote.StoryRemoteMediator
import com.syahdi.storyapp.data.response.ListStoryItem

class StoryRepository(private val storyDatabase: StoryDatabase, private val apiService: ApiService) {
    @OptIn(ExperimentalPagingApi::class)
    fun getStory(): LiveData<PagingData<ListStoryItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            remoteMediator = StoryRemoteMediator(storyDatabase, apiService),
            pagingSourceFactory = {
                // StoryPagingSource(apiService)
                storyDatabase.storyDao().getAllStories()
            }
        ).liveData
    }
}