package com.syahdi.storyapp.data.local.room

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.syahdi.storyapp.data.response.ListStoryItem

@Dao
interface StoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertStory(story: List<ListStoryItem>)

    @Query("SELECT * FROM story")
    fun getAllStories(): PagingSource<Int, ListStoryItem>

    @Query("DELETE FROM story")
    fun deleteAll(): Int
}