package com.syahdi.storyapp.ui.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.syahdi.storyapp.databinding.ItemStoryBinding
import com.syahdi.storyapp.data.response.ListStoryItem
import com.syahdi.storyapp.ui.detailStory.DetailActivity

class StoryListAdapter :
    PagingDataAdapter<ListStoryItem, StoryListAdapter.MyViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding, object : OnItemClickListener {
            override fun onItemClick(itemId: String) {
                val intent = Intent(parent.context, DetailActivity::class.java)
                intent.putExtra("id", itemId)
                parent.context.startActivity(intent)
            }
        })
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = getItem(position)
        if (data != null) {
            holder.bind(data)
        }
    }

    class MyViewHolder(
        private val binding: ItemStoryBinding,
        private val onItemClickListener: OnItemClickListener
    ) : RecyclerView.ViewHolder(binding.root) {

        private var currentItem: ListStoryItem? = null

        init {
            binding.root.setOnClickListener {
                currentItem?.let { item ->
                    onItemClickListener.onItemClick(item.id)
                }
            }
        }

        fun bind(data: ListStoryItem) {
            currentItem = data

            // Bind your data to the views
            Glide.with(binding.ivStoryPhoto.context)
                .load(data.photoUrl)
                .into(binding.ivStoryPhoto)
            binding.tvUserName.text = data.name
            binding.tvDateTime.text = data.createdAt
        }
    }

    interface OnItemClickListener {
        fun onItemClick(itemId: String)
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListStoryItem>() {
            override fun areItemsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}