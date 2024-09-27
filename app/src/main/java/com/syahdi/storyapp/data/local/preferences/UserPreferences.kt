package com.syahdi.storyapp.data.local.preferences

import android.content.Context
import android.content.SharedPreferences

class UserPreferences(context: Context){
    companion object {
        private const val PREFERENCES_FILE_NAME = "UserPreferences"
        private lateinit var sharedPreferences: SharedPreferences

        fun init(context: Context) {
            sharedPreferences = context.getSharedPreferences(PREFERENCES_FILE_NAME, Context.MODE_PRIVATE)
        }

        fun getString(key: String, defaultValue: String): String {
            return sharedPreferences.getString(key, defaultValue) ?: defaultValue
        }

        fun putString(key: String, value: String) {
            sharedPreferences.edit().putString(key, value).apply()
        }

        fun removeString(key: String) {
            sharedPreferences.edit().remove(key).apply()
        }
        // Add methods for other data types as needed, possible method of deleteString
    }

    init {
        init(context)
    }
}