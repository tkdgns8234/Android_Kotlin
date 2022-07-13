package com.hoon.body_calendar.ui.callback

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.hoon.body_calendar.model.RealtimeDatabase
import com.hoon.body_calendar.ui.adapter.MyRecyclerViewAdapter

class MyItemTouchHelperCallback(private val recyclerView: RecyclerView, val rdb: RealtimeDatabase) :
    ItemTouchHelper.SimpleCallback(
        ItemTouchHelper.UP or ItemTouchHelper.DOWN,
        ItemTouchHelper.LEFT
    ) {
    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        // onMove callback에서 어댑터의 위치를 계속 변환해야함
        // 따라서 adapterPosition 를 사용해 어댑터의 위치를 계속 추적해야한다.
        (recyclerView.adapter as MyRecyclerViewAdapter).moveItem(
            viewHolder.adapterPosition,
            target.adapterPosition
        )
        return true
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val id = (viewHolder as MyRecyclerViewAdapter.MyViewHolder).ID!!
        rdb.removeData(id)
        // layoutPosition 레이아웃 상에서 선택된 포지션 반환
        (recyclerView.adapter as MyRecyclerViewAdapter).removeItem(viewHolder.layoutPosition)
    }

    // 뷰 홀더 클릭 중 호출되는 콜백
    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        super.onSelectedChanged(viewHolder, actionState)
        when (actionState) {
            ItemTouchHelper.ACTION_STATE_DRAG, ItemTouchHelper.ACTION_STATE_SWIPE -> {
                (viewHolder as MyRecyclerViewAdapter.MyViewHolder).setAlpha(0.5f)
            }
        }
    }

    // 뷰 홀더 클릭을 놔줬을 때 호출되는 콜백
    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        super.clearView(recyclerView, viewHolder)
        (viewHolder as MyRecyclerViewAdapter.MyViewHolder).setAlpha(1.0f)
    }
}