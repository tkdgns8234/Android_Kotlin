package com.hoon.audiorecorder

import android.content.Context
import android.os.SystemClock
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

class CountUpView(
    context: Context,
    attrs: AttributeSet
) : AppCompatTextView(context, attrs) {

    private var startTimeStamp: Long = 0

    private val countUpAction: Runnable = object : Runnable {
        override fun run() {
            val currentTimeStamp = SystemClock.elapsedRealtime()
            val diffTimeStamp = currentTimeStamp - startTimeStamp
            updateCountTime(diffTimeStamp)
            handler.postDelayed(this, ACTION_INTERVAL)
        }
    }

    fun startCount() {
        // SystemClock.elapsedRealtime(): 시간 차이를 계산하기에 적합한 api
        // 부팅 이후의 소요 시간을 return하고 sleep time 까지 포함
        startTimeStamp = SystemClock.elapsedRealtime()
        handler.post(countUpAction)
    }

    fun stopCount() {
        handler.removeCallbacks(countUpAction)
    }

    fun clearCountTime() {
        updateCountTime(0)
    }

    private fun updateCountTime(timeMillis: Long) {
        val totalSeconds = (timeMillis / 1000).toInt()
        val minutes = totalSeconds / 60
        val seconds = totalSeconds % 60
        text = "%02d:%02d".format(minutes, seconds)
    }

    companion object {
        private const val ACTION_INTERVAL = 1000L
    }
}