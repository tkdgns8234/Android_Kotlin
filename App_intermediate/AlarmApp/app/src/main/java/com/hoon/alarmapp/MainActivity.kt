package com.hoon.alarmapp

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.hoon.alarmapp.databinding.ActivityMainBinding
import java.util.*

/*
어려웠던 점
    - sharedPreference 데이터와 앱 데이터 동기화
    - alarm 등록시 고려해야할 제약 사항들 (도즈모드, 시간 설정 등)
 */

class MainActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    lateinit var alarmModel: AlarmModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        alarmModel = fetchModelFromPreference()
        initViews()
        renderView()
    }

    private fun initViews() {
        binding.btnOnOff.setOnClickListener {
            alarmModel.isOn = alarmModel.isOn.not()

            saveTimeToPreference()
            renderView()

            if (alarmModel.isOn) {
                registerAlarm()
            } else {
                cancelAlarm()
            }
        }

        binding.btnChangeAlarmTime.setOnClickListener {
            val timerPickerDialog = setTimerPickerDialog()
            timerPickerDialog.show()
        }
    }

    private fun renderView() {
        binding.tvTime.text = alarmModel.displayTimeText
        binding.tvAmPm.text = alarmModel.amPmText
        binding.btnOnOff.text = alarmModel.onOffText
        binding.btnOnOff.tag = alarmModel
    }

    private fun setTimerPickerDialog() : TimePickerDialog{
        val calendar = Calendar.getInstance()

        return TimePickerDialog(this, TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
            alarmModel.apply {
                this.hour = hourOfDay
                this.minute = minute
                this.isOn = false
            }

            saveTimeToPreference()
            renderView()
            cancelAlarm()

        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false)
    }

    private fun registerAlarm() {
        val calendar = Calendar.getInstance().apply {
            // 시간을 지정했을 때, 이미 지난 시간인 경우 다음날로 설정
            set(Calendar.HOUR_OF_DAY, alarmModel.hour)
            set(Calendar.MINUTE, alarmModel.minute)
            if (before(Calendar.getInstance())) {
                add(Calendar.DATE, 1)
            }
        }

        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager

        val pendingIntent = PendingIntent.getBroadcast(
            this,
            ALARM_REQUEST_CODE,
            Intent(this, AlarmReceiver::class.java),
            PendingIntent.FLAG_CANCEL_CURRENT) // FLAG_CANCEL_CURRENT: intent가 이미 존재하는 경우 취소 후 생성

        alarmManager.setAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            pendingIntent
        )
    }

    private fun cancelAlarm() {
        val pendingIntent = PendingIntent.getBroadcast(
            this,
            ALARM_REQUEST_CODE,
            Intent(this, AlarmReceiver::class.java),
            PendingIntent.FLAG_NO_CREATE // 이미 생성된 pending intent 없으면 null 반환 있으면 해당 객체 반환
        )
        pendingIntent?.cancel()
    }

    private fun saveTimeToPreference() {
        val sharedPreferences = getSharedPreferences(packageName, MODE_PRIVATE)

        with(sharedPreferences.edit()) {
            putString(PREF_KEY_TIME, alarmModel.getTimeForDB())
            putBoolean(PREF_KEY_ONOFF, alarmModel.isOn)
            commit()
        }
    }

    private fun fetchModelFromPreference(): AlarmModel {
        val pref = getSharedPreferences(packageName, MODE_PRIVATE)
        val time = pref.getString(PREF_KEY_TIME, "00:00") ?: "00:00"
        val isOn = pref.getBoolean(PREF_KEY_ONOFF, false)

        val hourMinute = time.split(":")

        val model = AlarmModel(hourMinute[0].toInt(), hourMinute[1].toInt(), isOn)

        val pendingIntent = PendingIntent.getBroadcast(
            this, ALARM_REQUEST_CODE, Intent(this,
                AlarmReceiver::class.java), PendingIntent.FLAG_NO_CREATE)

        if ((pendingIntent == null) and model.isOn) {
            // 알람은 등록되어있지 않은데 UI 상 알람이 켜져있는 경우
            model.isOn = false
        } else if ((pendingIntent != null) and model.isOn.not()){
            // 알람은 등록되어있는데 UI 상 알람이 꺼져있는 경우
            pendingIntent.cancel()
        }

        return model
    }

    companion object {
        private const val PREF_KEY_TIME = "time"
        private const val PREF_KEY_ONOFF = "isOn"
        private const val ALARM_REQUEST_CODE = 1001
    }
}