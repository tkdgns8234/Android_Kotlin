package com.hoon.airbnb

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hoon.airbnb.databinding.ItemHotelViewpagerBinding

class HotelViewPagerAdapter(val onClick: (Hotel) -> Unit) : ListAdapter<Hotel, HotelViewPagerAdapter.ViewHolder>(diffUtil) {

    inner class ViewHolder(private val binding: ItemHotelViewpagerBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(hotel: Hotel) {
            with(binding) {
                tvTitle.text = hotel.title
                tvPrice.text = hotel.price
                Glide
                    .with(binding.root.context)
                    .load(hotel.imgUrl)
                    .into(ivHotel)
            }

            binding.root.setOnClickListener { onClick(hotel) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ItemHotelViewpagerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    companion object {
        private val diffUtil = object : DiffUtil.ItemCallback<Hotel>() {
            override fun areItemsTheSame(oldItem: Hotel, newItem: Hotel): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Hotel, newItem: Hotel): Boolean {
                return oldItem == newItem
            }

        }
    }
}