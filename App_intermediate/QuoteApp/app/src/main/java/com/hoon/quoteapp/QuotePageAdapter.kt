package com.hoon.quoteapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hoon.quoteapp.databinding.ItemQuoteBinding

class QuotePageAdapter(
    private val quotes: List<Quote>,
    private val isNameRevealed: Boolean
) : RecyclerView.Adapter<QuotePageAdapter.QuoteViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuoteViewHolder {
        val binding = ItemQuoteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return QuoteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: QuoteViewHolder, position: Int) {
        val actualPosition = position % quotes.size // 무한 swipe 처리를 위함
        holder.bind(quotes[actualPosition], isNameRevealed)
    }

    override fun getItemCount(): Int {
        return Int.MAX_VALUE // 무한 swipe 처리를 위함
    }


    class QuoteViewHolder(private val binding: ItemQuoteBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(quote: Quote, isNameRevealed: Boolean) {
            binding.tvQuote.text = "\"${quote.quote}\""

            if (isNameRevealed) {
                binding.tvName.text = "- ${quote.name}"
                binding.tvName.visibility = View.VISIBLE
            } else {
                binding.tvName.visibility = View.GONE
            }
        }
    }
}