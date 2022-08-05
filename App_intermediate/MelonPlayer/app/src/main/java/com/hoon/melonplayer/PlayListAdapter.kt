package com.hoon.melonplayer

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hoon.melonplayer.databinding.ItemMusicBinding
import com.hoon.melonplayer.model.MusicModel

class PlayListAdapter(val onClick: (index: Int) -> Any) : ListAdapter<MusicModel, PlayListAdapter.ViewHolder>(diffutils) {

    inner class ViewHolder(private val binding: ItemMusicBinding) :RecyclerView.ViewHolder(binding.root){

        fun bind(musicModel: MusicModel) {
            binding.root.setOnClickListener { onClick(musicModel.id) }
            binding.tvMusicTitle.text = musicModel.title
            binding.tvArtist.text = musicModel.artist

            Glide
                .with(binding.root.context)
                .load(musicModel.coverImageURL)
                .into(binding.coverImageView)

            if (musicModel.isPlayingStatus) {
                binding.root.setBackgroundColor(Color.GRAY)
            } else {
                binding.root.setBackgroundColor(Color.TRANSPARENT)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayListAdapter.ViewHolder {
        val binding = ItemMusicBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PlayListAdapter.ViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    companion object {
        private val diffutils = object : DiffUtil.ItemCallback<MusicModel>() {
            override fun areItemsTheSame(oldItem: MusicModel, newItem: MusicModel): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: MusicModel, newItem: MusicModel): Boolean {
                return oldItem == newItem
            }

        }
    }
}