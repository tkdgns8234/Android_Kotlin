package com.hoon.simplewebbrowser

import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.webkit.URLUtil
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.TextView
import com.hoon.simplewebbrowser.databinding.ActivityMainBinding

/*
배운점
- webView 사용해보기
- constraintLayout의 ratio 설정
- ?attr/ 태그를 활용해 간단한 버튼 리플 효과 주기
- SwipeRefreshLayout 사용하기
- contents loading progress bar 사용 시 최소한의 progress time 설정 가능
 */

class MainActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initViews()
        bindingViews()
    }

    override fun onBackPressed() {
        if (binding.webView.canGoBack()) {
            binding.webView.goBack()
        } else {
            super.onBackPressed()
        }
    }

    private fun initViews() {
        binding.webView.apply {
            webViewClient = CustomWebViewClient()
            webChromeClient = CustomWebChromeClient()
            settings.javaScriptEnabled = true
            loadUrl(DEFAULT_URL)
        }
    }

    private fun bindingViews() {
        binding.addressBar.setOnEditorActionListener(TextView.OnEditorActionListener
        { v, actionId, event ->

            if (actionId == EditorInfo.IME_ACTION_DONE) {
                val loadURL = v.text.toString()
                if (URLUtil.isNetworkUrl(loadURL)) {
                    binding.webView.loadUrl(loadURL)
                }
                else {
                    binding.webView.loadUrl("http://$loadURL")
                }
            }

            return@OnEditorActionListener false
        })

        binding.layoutRefresh.setOnRefreshListener {
            binding.webView.reload()
        }

        binding.btnBack.setOnClickListener {
            binding.webView.goBack()
        }

        binding.btnForward.setOnClickListener {
            binding.webView.goForward()
        }

        binding.btnHome.setOnClickListener {
            binding.webView.loadUrl(DEFAULT_URL)
        }
    }

    inner class CustomWebViewClient : WebViewClient() {

        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            super.onPageStarted(view, url, favicon)
            binding.progressBar.show()
        }

        // 페이지가 모두 로딩됐을 때
        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)

            binding.progressBar.hide()
            binding.layoutRefresh.isRefreshing = false

            binding.btnBack.isEnabled = binding.webView.canGoBack()
            binding.btnForward.isEnabled = binding.webView.canGoForward()
            binding.addressBar.setText(url)
        }
    }

    inner class CustomWebChromeClient : WebChromeClient() {
        //current page loading progress
        override fun onProgressChanged(view: WebView?, newProgress: Int) {
            super.onProgressChanged(view, newProgress)
            binding.progressBar.progress = newProgress
        }
    }

    companion object {
        private const val DEFAULT_URL = "http://www.naver.com"
    }
}