package com.hoon.pomodorotimer

import android.media.SoundPool
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.widget.SeekBar
import com.hoon.pomodorotimer.databinding.ActivityMainBinding
import kotlin.math.min

/*
배운점
- seekbar 활용
- timer 활용
- sound pool 활용
- constraint 레이아웃 baseline 활용
- alpha 값을 조정하여 transparent 색상 만들기
- vector 사용 이유
-> 안드로이드 여러 해상도 지원해야함
-> vector를 이용해 이미지파일 코드로 제공 -> 유연하게 대응 가능
-> 장점: apk 용량이 줄어듦
-> 제약: 150 * 150 이하의 img 파일만 설정하도록 권고
https://developer.android.com/studio/write/vector-asset-studio?hl=ko
 */

class MainActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private var countDownTimer: CountDownTimer? = null

    // 메모리에 압축되지 않은 형태로 sound를 load 해서 사용
    // 일반적인 audiotrack, mediaplayer를 이용한 방법보다 빠름
    // 단 , 1MB 이하의 음원 파일만 재생 가능
    private var soundPool = SoundPool.Builder().build()
    private var tickingSoundID: Int? = null
    private var bellSoundID: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initViews()
        initSoundPool()
    }

    override fun onResume() {
        super.onResume()
        soundPool.autoResume()
    }

    override fun onPause() {
        super.onPause()
        soundPool.autoPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        soundPool.release()
    }

    private fun initSoundPool() {
        tickingSoundID = soundPool.load(this, R.raw.timer_ticking, 1)
        bellSoundID = soundPool.load(this, R.raw.timer_bell, 1)
    }

    private fun initViews() {
        binding.seekBar.setOnSeekBarChangeListener(
            object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    if (fromUser) {
                        updateTimeTextView(progress * 60 * 1000L)
                    }
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {
                    stopCountDown()
                }

                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                    seekBar ?: return

                    if (seekBar.progress == 0) {
                        stopCountDown()
                    } else {
                        startCountDown(seekBar)
                    }
                }
            }
        )
    }

    private fun createCountDownTimer(initialMillis: Long): CountDownTimer {
        return object : CountDownTimer(initialMillis, 1000L) {
            override fun onTick(millisUntilFinished: Long) {
                updateTimeTextView(millisUntilFinished)
                updateSeekBar(millisUntilFinished)
            }

            override fun onFinish() {
                completeCountDown()
            }
        }
    }

    private fun startCountDown(seekBar: SeekBar) {
        countDownTimer = createCountDownTimer(seekBar.progress * 60 * 1000L)
        countDownTimer?.start()

        tickingSoundID?.let {
            soundPool.play(it, 1F, 1F, 0, -1, 1F)
        }
    }

    private fun completeCountDown() {
        updateTimeTextView(0)
        updateSeekBar(0)

        soundPool.autoPause()
        bellSoundID?.let {
            soundPool.play(it, 1F, 1F, 0, 0, 1F)
        }
    }

    private fun stopCountDown() {
        countDownTimer?.cancel()
        countDownTimer = null
        soundPool.autoPause()
    }

    private fun updateTimeTextView(millisecond: Long) {
        val second = millisecond / 1000
        binding.tvRemainMinutes.text = "%02d'".format(second / 60)
        binding.tvRemainSeconds.text = "%02d".format(second % 60)
    }

    private fun updateSeekBar(millisecond: Long) {
        val minute = (millisecond / 1000 / 60).toInt()
        binding.seekBar.progress = minute
    }
}