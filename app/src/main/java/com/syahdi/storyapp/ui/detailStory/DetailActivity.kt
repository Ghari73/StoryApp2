package com.syahdi.storyapp.ui.detailStory

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.syahdi.storyapp.R
import com.syahdi.storyapp.databinding.ActivityDetailBinding
import com.syahdi.storyapp.data.local.preferences.UserPreferences
import com.syahdi.storyapp.data.response.Story
import java.text.SimpleDateFormat
import java.util.*

class DetailActivity : AppCompatActivity() {

    private lateinit var detailViewModel: DetailViewModel
    private lateinit var binding: ActivityDetailBinding
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        progressBar = findViewById(R.id.progressBarDetail)
        progressBar.visibility = View.GONE

        setupViewModel()
        setupAction()
    }

    private fun setupViewModel() {
        detailViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        )[DetailViewModel::class.java]
    }

    private fun setupAction() {
        progressBar.visibility = View.VISIBLE
        val token = UserPreferences.getString("token", "")
        if (token != "") {
            val id = if (Build.VERSION.SDK_INT >= 33) {
                intent.getStringExtra("id")
            } else {
                @Suppress("DEPRECATION")
                intent.getStringExtra("id")
            }

            if (id != null) {
                detailViewModel.getStory(token, id)
                detailViewModel.story.observe(this) { story ->
                    setDetailStoryData(story)
                }
            }
        }
    }

    private fun setDetailStoryData(item: Story) {
        val imgStoryPhoto: ImageView = findViewById(R.id.iv_detail_story_photo)
        val tvUserName: TextView = findViewById(R.id.tv_detail_user_name)
        val tvDateTime: TextView = findViewById(R.id.tv_detail_date_time)
        val tvStoryDescription: TextView = findViewById(R.id.tv_detail_story_description)

        val dateTimeFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        dateTimeFormat.timeZone = TimeZone.getTimeZone("UTC")

        Glide.with(this)
            .load(item.photoUrl)
            .into(imgStoryPhoto)
        tvUserName.text = item.name
        tvDateTime.text = dateTimeFormat
            .parse(item.createdAt)?.toString()
        tvStoryDescription.text = item.description
        progressBar.visibility = View.GONE
    }
}