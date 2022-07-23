package com.hoon.quoteapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.viewpager2.widget.ViewPager2
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import com.hoon.quoteapp.databinding.ActivityMainBinding
import org.json.JSONArray
import org.json.JSONObject
import kotlin.math.absoluteValue

/*
배운점
 - Firebase remoteConfig 사용하기
    - 앱 업데이트 없이 앱의 일부 내용 변경 가능
 - viewPager2 사용하기
    - Fragment 가 아닌 일반적인 view 를 위치시키는 경우 RecyclerView 구현과 동일하다
    - PageTransformer 를 이용해서 페이지 이동 시 효과 주기
    - 무한 스크롤링 구현하기
 */

class MainActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initViews()
        initData()
    }

    private fun initViews() {
        binding.viewPager.setPageTransformer( ViewPager2.PageTransformer { page, position ->
            when (position) {
                0F -> {
                    page.alpha = 1F
                }
                else -> {
                    page.alpha = 1F - position.absoluteValue * 2
                }
            }
        })
    }

    private fun initData() {
        val remoteConfig = Firebase.remoteConfig
        remoteConfig.setConfigSettingsAsync(
            remoteConfigSettings {
                minimumFetchIntervalInSeconds = 0 // server에서 block하지 않는 이상 바로바로 패치가 적용 되도록 설정
            }
        )

        // 서버와 통신하기 때문에 비동기로 동작, 리스너를 등록해서 통신
        remoteConfig.fetchAndActivate().addOnCompleteListener {
            binding.progressBar.visibility = View.GONE
            if (it.isSuccessful) {
                val quotes = parseQuotesJson(remoteConfig.getString(REMOTE_KEY_QUOTES))
                val isNameRevealed = remoteConfig.getBoolean(REMOTE_KEY_ISNAMEREVEALED)
                displayQuotesPage(quotes, isNameRevealed)
            } else {
                Log.w(TAG, "remoteConfig fetchAndActivate() task failed")
            }
        }
    }

    private fun displayQuotesPage(quotes: List<Quote>, isNameRevealed: Boolean) {
        val adapter = QuotePageAdapter(quotes, isNameRevealed)
        binding.viewPager.adapter = adapter
        binding.viewPager.setCurrentItem(adapter.itemCount/2, false) // 무한 swipe 처리를 위함
    }

    private fun parseQuotesJson(json: String): List<Quote> {
        val jsonArray = JSONArray(json)
        var jsonList = emptyList<JSONObject>()
        for (i in 0 until jsonArray.length()) {
            val jsonObject = jsonArray.getJSONObject(i)
            jsonObject?.let {
                jsonList = jsonList + it
            }
        }

        return jsonList.map {
            Quote(
                it.getString("quote"),
                it.getString("name")
            )
        }
    }

    companion object {
        private const val TAG = "MainActivity"
        private const val REMOTE_KEY_QUOTES = "quotes"
        private const val REMOTE_KEY_ISNAMEREVEALED = "is_name_revealed"
    }
}