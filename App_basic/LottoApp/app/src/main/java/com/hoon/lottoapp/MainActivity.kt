package com.hoon.lottoapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.hoon.lottoapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    /*
     중복 제거를 위해 set 사용!
        데이터 정렬이 필요없고, 성능이 가장 좋은 hashset 사용! (내부적으로 hashtable 에 데이터 저장)
        treeset -> 데이터 value에 따라 정렬됨, rbtree 로 구현됨
        listset -> 저장된 순서에따라 저장, 셋중 가장 느림
     */
    private val pickNumberSet = hashSetOf<Int>()

    // 데이터 접근 용도로만 사용할거기때문에 ArrayList를 사용하자 ~_~ O(1)
    private val numberTextViewList: ArrayList<TextView> by lazy {
        // apply 는 수신객체를 return!
        arrayListOf<TextView>().apply {
            add(binding.lottoNum1)
            add(binding.lottoNum2)
            add(binding.lottoNum3)
            add(binding.lottoNum4)
            add(binding.lottoNum5)
            add(binding.lottoNum6)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initViews()
    }

    private fun initViews() {
        binding.numberPicker.minValue = 1
        binding.numberPicker.maxValue = 45

        binding.btnAutoCreate.setOnClickListener { initAutoCreateBtn() }
        binding.btnAddNumber.setOnClickListener { initAddBtn() }
        binding.btnClear.setOnClickListener { initClearBtn() }
    }

    private fun initAutoCreateBtn() {
        val list = getRandomNumber()
        list.forEachIndexed { index, number ->
            val textView = numberTextViewList[index]

            textView.isVisible = true
            textView.text = number.toString()
            setLottoNumberBackground(textView, number)
        }
    }

    private fun initClearBtn() {
        pickNumberSet.clear()
        numberTextViewList.forEach {
            it.isVisible = false
        }
    }

    private fun initAddBtn() {
        if (pickNumberSet.size >= 5) {
            Toast.makeText(this, "번호는 5개까지만 입력 가능합니다", Toast.LENGTH_SHORT).show()
            return
        }

        if (pickNumberSet.contains(binding.numberPicker.value)) {
            Toast.makeText(this, "이미 선택한 번호입니다.", Toast.LENGTH_SHORT).show()
            return
        }

        val number = binding.numberPicker.value

        val textView = numberTextViewList[pickNumberSet.size]
        textView.isVisible = true
        textView.text = number.toString()

        pickNumberSet.add(number)
        setLottoNumberBackground(textView, number)
    }

    private fun setLottoNumberBackground(textView: TextView, number: Int) {
        when (number) {
            in 1..10 -> textView.background =
                ContextCompat.getDrawable(this, R.drawable.circle_yellow)
            in 11..20 -> textView.background =
                ContextCompat.getDrawable(this, R.drawable.circle_blue)
            in 21..30 -> textView.background =
                ContextCompat.getDrawable(this, R.drawable.circle_red)
            in 31..40 -> textView.background =
                ContextCompat.getDrawable(this, R.drawable.circle_gray)
            else -> textView.background =
                ContextCompat.getDrawable(this, R.drawable.circle_gray)
        }
    }

    private fun getRandomNumber(): List<Int> {
        val list = arrayListOf<Int>().apply {
            for (i in 1..45) {
                if (pickNumberSet.contains(i)) {
                    continue
                }
                this.add(i)
            }
        }

        list.shuffle()
        val newList = pickNumberSet.toList() + list.subList(0, 6 - pickNumberSet.size)
        return newList.sorted()
    }
}