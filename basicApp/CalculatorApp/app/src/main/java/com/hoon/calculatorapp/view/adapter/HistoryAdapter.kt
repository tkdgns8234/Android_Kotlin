package com.hoon.calculatorapp.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hoon.calculatorapp.database.History
import com.hoon.calculatorapp.databinding.ItemCalculationHistoryBinding

class HistoryAdapter(private var item: MutableList<History>) : RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {
    class ViewHolder(private val binding: ItemCalculationHistoryBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(history: History) {
            binding.tvExpression.text = history.expression
            binding.tvResult.text = history.result
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCalculationHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return item.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(item[position])
    }

    fun clearItem() {
        item.clear()
    }

    fun setItem(_item: MutableList<History>) {
        item = _item
    }
}