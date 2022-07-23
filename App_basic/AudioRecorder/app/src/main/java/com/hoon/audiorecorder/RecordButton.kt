package com.hoon.audiorecorder

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageButton

// customView 가이드 : https://developer.android.com/training/custom-views/create-view

class RecordButton(
    context: Context,
    attrs: AttributeSet
) : AppCompatImageButton(context, attrs) { // 하위 호환성 보장을 위한 AppCompatImageButton 사용

    init {
        setBackgroundResource(R.drawable.shape_oval_button)
    }

    fun updateIconWithState(state: State) {
        when (state) {
            State.BEFORE_RECORDING -> {
                setImageResource(R.drawable.ic_record_24)
            }
            State.ON_RECORDING -> {
                setImageResource(R.drawable.ic_stop_24)
            }
            State.AFTER_RECORDING -> {
                setImageResource(R.drawable.ic_play_24)
            }
            State.ON_PLAYING -> {
                setImageResource(R.drawable.ic_stop_24)
            }
        }
    }
}