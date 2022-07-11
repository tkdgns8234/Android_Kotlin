package com.hoon.listview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
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
        var convertView = convertView

        if (convertView == null) {
            convertView = LayoutInflater.from(parent?.context).inflate(R.layout.item_list_view, parent, false)
        }

        val tv1 = convertView?.findViewById<TextView>(R.id.tv_1)
        val tv2 = convertView?.findViewById<TextView>(R.id.tv_2)

        tv1?.text = customList[position].textView1
        tv2?.text = customList[position].textView2

        return convertView!!
    }
}