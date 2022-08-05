package com.hoon.airbnb.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.hoon.airbnb.model.Hotel
import com.hoon.airbnb.databinding.ItemHotelListBinding

class HotelListAdapter : ListAdapter<Hotel, HotelListAdapter.ViewHolder>(diffUtil) {

    inner class ViewHolder(private val binding: ItemHotelListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(hotel: Hotel) {
            with(binding) {
                tvTitle.text = hotel.title
                tvPrice.text = hotel.price
                Glide
                    .with(binding.root.context)
                    .load(hotel.imgUrl)
                    .transform(
                        CenterCrop(),
                        RoundedCorners(dpToPixel(binding.root.context, 15))
                    )
                    .into(ivHotel)
            }
        }

        private fun dpToPixel(context: Context, dp: Int): Int {
            val density = context.resources.displayMetrics.density // 화면 밀도
            return (density * dp).toInt()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ItemHotelListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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