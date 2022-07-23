package com.hoon.quoteapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hoon.quoteapp.databinding.ItemQuoteBinding

class QuotePageAdapter(
    private val quotes: List<Quote>
) : RecyclerView.Adapter<QuotePageAdapter.QuoteViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuoteViewHolder {
        val binding = ItemQuoteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return QuoteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: QuoteViewHolder, position: Int) {
        holder.bind(quotes[position])
    }

    override fun getItemCount(): Int {
        return quotes.size
    }


    class QuoteViewHolder(private val binding: ItemQuoteBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(quote: Quote) {
            binding.tvQuote.text = quote.quote
            binding.tvName.text = quote.name
        }
    }
}