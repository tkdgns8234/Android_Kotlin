package com.hoon.body_calendar.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.hoon.body_calendar.R
import com.hoon.body_calendar.model.Memo

class MyListViewAdapter(val memoList: MutableList<Memo>) : BaseAdapter(){
    override fun getCount(): Int {
        return memoList.size
    }

    override fun getItem(position: Int): Any {
        return memoList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var convertView = convertView

        if (convertView == null) {
            convertView = LayoutInflater.from(parent?.context).inflate(R.layout.item_my_list_view, parent, false)
        }
        val btnDate = convertView?.findViewById<TextView>(R.id.tv_listViewItem_date)
        val btnMemo = convertView?.findViewById<TextView>(R.id.tv_listViewItem_memo)

        btnDate?.text = memoList[position].date
        btnMemo?.text = memoList[position].memo

        return convertView!!
    }
}