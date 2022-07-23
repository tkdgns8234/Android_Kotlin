package com.example.simplegallery

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // main thread 에 sleep 주는건 매우 위험! (ANR 발생 가능)
        // handler를 통해 mainlooper(Ui thread)와 연결하고 3초뒤 작업이 실행되도록 요청!
        Handler(mainLooper).postDelayed(Runnable {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }, 2000)
    }
}