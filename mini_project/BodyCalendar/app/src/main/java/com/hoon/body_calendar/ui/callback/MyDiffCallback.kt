package com.hoon.body_calendar.ui.callback

import androidx.recyclerview.widget.DiffUtil
import com.hoon.body_calendar.model.Memo

class MyDiffCallback : DiffUtil.ItemCallback<Memo>() {
    override fun areItemsTheSame(oldItem: Memo, newItem: Memo): Boolean {
        return oldItem.hashCode() == newItem.hashCode()
    }

    override fun areContentsTheSame(oldItem: Memo, newItem: Memo): Boolean {
        return oldItem == newItem
    }
}