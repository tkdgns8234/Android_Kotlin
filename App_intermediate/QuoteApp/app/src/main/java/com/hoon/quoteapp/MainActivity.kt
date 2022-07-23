package com.hoon.quoteapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.hoon.quoteapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    private val quotes: MutableList<Quote> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initViews()
    }

    private fun initViews() {
        quotes.add(Quote("test","test"))
        val adapter = QuotePageAdapter(quotes)
        binding.viewPager.adapter = adapter
    }
}