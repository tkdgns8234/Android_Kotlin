package com.hoon.body_calendar.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hoon.body_calendar.databinding.ItemMyListViewBinding
import com.hoon.body_calendar.model.Memo
import com.hoon.body_calendar.ui.callback.MyDiffCallback
import java.util.*

class MyRecyclerViewAdapter : ListAdapter<Memo, RecyclerView.ViewHolder>(MyDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(
            ItemMyListViewBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is MyViewHolder) {
            val memo = getItem(position) as Memo
            holder.bind(memo)
        }
    }

    fun moveItem(fromPosition: Int, toPosition: Int) {
        val newList = currentList.toMutableList()
        Collections.swap(newList, fromPosition, toPosition)
        submitList(newList)
    }

    fun removeItem(position: Int) {
        val newList = currentList.toMutableList()
        newList.removeAt(position)
        submitList(newList)
    }

    class MyViewHolder(val binding: ItemMyListViewBinding) : RecyclerView.ViewHolder(binding.root) {
        var ID : Long? = null
        fun bind(memo: Memo) {
            with(binding) {
                tvListViewItemDate.text = memo.date
                tvListViewItemMemo.text = memo.memo
                ID = memo.ID
            }
        }

        fun setAlpha(alpha: Float) {
            with(binding) {
                tvListViewItemDate.alpha = alpha
                tvListViewItemMemo.alpha = alpha
            }
        }
    }
}