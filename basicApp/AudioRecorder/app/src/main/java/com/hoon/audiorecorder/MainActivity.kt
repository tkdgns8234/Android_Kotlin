package com.hoon.audiorecorder

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.hoon.audiorecorder.databinding.ActivityMainBinding

/*
배운점
- custom View(UI) 만들어보기
- intent의 data 표현 방법 중 uri로 표현하는 방법 알아보기
    - scheme + host
    - scheme + ssp(scheme-specific-part)
    -> https://codechacha.com/ko/android-uri-ssp/
- 앱의 권한 요청하기
- 앱에서 접근하는 storage
    - internal storage 앱 저장 공간, 외부 접근 불가, 공간이 작음
    - external storage 외부 접근 가능, mp3등 용량 큰 데이터 저장에 적합
- 미디어파일과 같은 용량이 큰 데이터는 사용하지 않는게 명확할 시, 참조를 null로 변경하여 메모리 관리
- 코틀린 setter 활용하기
- 람다를 이용한 callback 등록
 */

class MainActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val requiredPermissions = arrayOf(Manifest.permission.RECORD_AUDIO)
    private val recordingFilePath by lazy {
        "${externalCacheDir?.absolutePath}/recording.3gp"
    }
    private var recorder: MediaRecorder? = null
    private var player: MediaPlayer? = null
    private var state = State.BEFORE_RECORDING
        set(value) {
            field = value // 무한 재귀에 빠지는것을 막기 위해 field 키워드 사용
            binding.btnReset.isEnabled = value == State.AFTER_RECORDING ||
                    value == State.ON_PLAYING
            binding.btnRecord.updateIconWithState(value)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initViews()
        initVariable()
    }

    override fun onStart() {
        super.onStart()
        requestAudioPermission()
    }

    private fun initViews() {
        binding.btnRecord.updateIconWithState(state)
        binding.btnRecord.setOnClickListener {
            when (state) {
                State.BEFORE_RECORDING -> startRecording()
                State.ON_RECORDING -> stopRecording()
                State.AFTER_RECORDING -> startPlaying()
                State.ON_PLAYING -> stopPlaying()
            }
        }
        binding.btnReset.setOnClickListener {
            stopPlaying()
            binding.soundVisualizerView.clearVisualizing()
            binding.tvRecordTime.clearCountTime()
            state = State.BEFORE_RECORDING
        }
        binding.soundVisualizerView.onRequestCurrentBitDepth = {
            recorder?.maxAmplitude ?: 0
        }
    }

    private fun initVariable() {
        state = State.BEFORE_RECORDING // setter 호출을 위함
    }

    private fun startRecording() {
        recorder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            MediaRecorder(this)
        } else {
            MediaRecorder()
        }

        recorder?.apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
            // 따로 저장하지 않고 사용하기때문에 cache directory를 사용
            // (시스템에서 용량 확보 필요 시 cache에 있는것들은 쉽게 지워질 수 있음)
            setOutputFile(recordingFilePath)
            prepare()
        }
        recorder?.start()
        binding.soundVisualizerView.startVisualizing(false)
        binding.tvRecordTime.startCount()
        state = State.ON_RECORDING
    }

    private fun stopRecording() {
        recorder?.run {
            stop()
            release()
        }
        recorder = null
        binding.soundVisualizerView.stopVisualizing()
        binding.tvRecordTime.stopCount()
        state = State.AFTER_RECORDING
    }

    private fun startPlaying() {
        player = MediaPlayer()
            .apply {
                setDataSource(recordingFilePath)
                prepare()
            }
        player?.setOnCompletionListener {
            stopPlaying()
        }

        player?.start()
        binding.soundVisualizerView.startVisualizing(true)
        binding.tvRecordTime.startCount()
        state = State.ON_PLAYING
    }

    private fun stopPlaying() {
        player?.release()
        player = null
        binding.soundVisualizerView.stopVisualizing()
        binding.tvRecordTime.stopCount()
        state = State.AFTER_RECORDING
    }

    private fun requestAudioPermission() {
        requestPermissions(requiredPermissions, REQUEST_RECORD_AUDIO_PERMISSION)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        val audioRecordPermissionGranted =
            requestCode == REQUEST_RECORD_AUDIO_PERMISSION &&
                    grantResults.firstOrNull() == PackageManager.PERMISSION_GRANTED

        if (!audioRecordPermissionGranted) {
            againRequestPermission()
        }
    }

    private fun againRequestPermission() {
        val snackbar = Snackbar.make(
            binding.root,
            "녹음 권한이 필요합니다. 확인을 누르면 설정 화면으로 이동합니다.", Snackbar.LENGTH_INDEFINITE
        )
        snackbar.setAction(
            "확인"
        ) {
            val intent = Intent()
            intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
            val uri = Uri.fromParts("package", packageName, null)
            intent.data = uri
            startActivity(intent)
        }
        snackbar.show()
    }

    companion object {
        private const val REQUEST_RECORD_AUDIO_PERMISSION = 101
    }
}