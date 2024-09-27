package com.syahdi.storyapp.ui.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.syahdi.storyapp.R
import com.syahdi.storyapp.data.response.ListStoryItem
import com.syahdi.storyapp.ui.detailStory.DetailActivity
import java.text.SimpleDateFormat
import java.util.*

class StoryAdapter(private val listStory: List<ListStoryItem>) :  RecyclerView.Adapter<StoryAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_story, parent, false))

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        Glide.with(viewHolder.itemView.context)
            .load(listStory[position].photoUrl)
            .into(viewHolder.storyPhoto)

        viewHolder.userName.text = listStory[position].name

        val dateTimeString = listStory[position].createdAt
        val dateTimeFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        dateTimeFormat.timeZone = TimeZone.getTimeZone("UTC")
        val reformattedDateTime : String? = dateTimeFormat.parse(dateTimeString)?.toString()
        viewHolder.dateTime.text = reformattedDateTime

        viewHolder.itemView.setOnClickListener {
            val intentDetail = Intent(viewHolder.itemView.context, DetailActivity::class.java)
            intentDetail.putExtra("id", listStory[position].id)
            viewHolder.itemView.context.startActivity(intentDetail)
        }
    }

    override fun getItemCount() = listStory.size

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val storyPhoto: ImageView = view.findViewById(R.id.iv_story_photo)
        val userName: TextView = view.findViewById(R.id.tv_user_name)
        val dateTime: TextView = view.findViewById(R.id.tv_date_time)
    }
}