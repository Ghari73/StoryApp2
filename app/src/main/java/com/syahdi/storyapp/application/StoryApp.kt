package com.syahdi.storyapp.application

import android.app.Application
import com.syahdi.storyapp.data.local.preferences.UserPreferences

class StoryApp : Application() {

    override fun onCreate() {
        super.onCreate()
        UserPreferences.init(applicationContext)
    }

}