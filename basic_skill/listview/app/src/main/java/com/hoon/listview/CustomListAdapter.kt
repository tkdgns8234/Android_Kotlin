package com.hoon.listview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.hoon.listview.databinding.ItemListViewBinding

class CustomListAdapter(val customList : MutableList<CustomModel>) : BaseAdapter() {
    override fun getCount(): Int {
        return customList.size
    }

    override fun getItem(position: Int): Any {
        return customList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val binding = ItemListViewBinding.inflate(LayoutInflater.from(parent?.context), parent, false)

        binding.tv1.text = customList[position].textView1
        binding.tv2.text = customList[position].textView2

        return binding.root
    }
}