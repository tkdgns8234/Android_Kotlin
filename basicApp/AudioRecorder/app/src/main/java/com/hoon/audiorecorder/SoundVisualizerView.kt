package com.hoon.audiorecorder

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.os.Handler
import android.util.AttributeSet
import android.view.View
import kotlin.random.Random

/*
canvas : 무엇을 그릴건지 (line, rectangle 등)
paint : 어떻게 그릴건지 (색상, 폰트 등)

주의 사항
onDraw는 매우 빈번하게 호출됨, Paint등 객체 초기화를 해선 안됨
사이즈 변화에 대응하기 위해 onSizeChanged 함수 오버라이딩

자세한내용은 developer 참조
 */

class SoundVisualizerView(
    context: Context,
    attrs: AttributeSet? = null
) : View(context, attrs) {

    var onRequestCurrentBitDepth: (() -> Int)? = null

    // ANTI_ALIAS_FLAG -> 곡선을 좀 더 부드럽게 표현
    private val bitDepthPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = context.getColor(R.color.purple_500)
        strokeWidth = LINE_WIDTH
        strokeCap = Paint.Cap.ROUND
    }

    private var drawingWidth: Int = 0
    private var drawingHeight: Int = 0
    private var drawingBitDepth: List<Int> = emptyList()
    private var isReplaying: Boolean = false
    private var replayingPosition: Int = 0

    private val visualizerRepeatAction: Runnable = object : Runnable {
        override fun run() {
            if (isReplaying) {
                val currBitDepth = onRequestCurrentBitDepth?.invoke() ?: 0
                drawingBitDepth =  listOf(currBitDepth) + drawingBitDepth
            } else {
                replayingPosition++
            }
            invalidate()

            handler.postDelayed(this, ACTION_INTERVAL)
        }
    }


    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        drawingWidth = w
        drawingHeight = h
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        canvas ?: return

        val centerY = drawingHeight / 2f // 높이의 절반
        var offsetX = drawingWidth.toFloat()  // 가장 우측

        drawingBitDepth
            .let { bitDepth ->
                if (isReplaying) {
                    bitDepth.takeLast(replayingPosition)
                } else {
                    bitDepth
                }
            }
            .forEach { bitDepth ->
                val lineLength = bitDepth / MAX_BIT_DEPTH * drawingHeight * 0.8f

                offsetX -= LINE_SPACE
                if (offsetX < 0) return@forEach

                canvas.drawLine(
                    offsetX,
                    centerY - lineLength / 2F, // centerY를 기준으로 파형을 그림
                    offsetX,
                    centerY + lineLength / 2F,
                    bitDepthPaint
                )
            }
    }

    fun startVisualizing(isReplaying: Boolean) {
        this.isReplaying = isReplaying
        handler.post(visualizerRepeatAction)
    }

    fun stopVisualizing() {
        replayingPosition = 0
        handler.removeCallbacks(visualizerRepeatAction)
    }

    fun clearVisualizing() {
        drawingBitDepth = emptyList()
        invalidate()
    }

    companion object {
        private const val LINE_WIDTH = 10F
        private const val LINE_SPACE = 15F
        private const val MAX_BIT_DEPTH = Short.MAX_VALUE.toFloat()
        private const val ACTION_INTERVAL = 20L
    }
}