package com.hoon.calculatorapp.view.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.hoon.calculatorapp.R
import java.lang.NumberFormatException
import com.hoon.calculatorapp.databinding.ActivityMainBinding
import com.hoon.calculatorapp.database.History
import com.hoon.calculatorapp.database.HistoryDataBase
import com.hoon.calculatorapp.view.adapter.HistoryAdapter


// TODO:
//  1. RecyclerVIew를 ListAdapter를 상속하는 클래스로 변경(DiffUtills 이용)
//  2. data binding 사용하기
//  3. MVVM 구조로 변경

class MainActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    lateinit var db: HistoryDataBase
    lateinit var historyAdapter: HistoryAdapter

    // 이전에 입력한 값이 operation 인 경우
    private var isOperator = false

    // operation을 이미 포함한 경우
    private var hasOperator = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        db = Room.databaseBuilder(
            applicationContext,
            HistoryDataBase::class.java,
            "HistoryDB"
        ).build()

        setRecyclerView()
    }

    private fun setRecyclerView() {
        Thread(Runnable {
            val historyList = db.historyDao().getAll()
            historyAdapter = HistoryAdapter(historyList)
            runOnUiThread {
                with(binding.rvHistory) {
                    layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                    adapter = historyAdapter
                }
            }
        }).start()
    }

    fun btnClicked(view: View) {
        when (view.id) {
            R.id.btn0 -> numberBtnClicked("0")
            R.id.btn1 -> numberBtnClicked("1")
            R.id.btn2 -> numberBtnClicked("2")
            R.id.btn3 -> numberBtnClicked("3")
            R.id.btn4 -> numberBtnClicked("4")
            R.id.btn5 -> numberBtnClicked("5")
            R.id.btn6 -> numberBtnClicked("6")
            R.id.btn7 -> numberBtnClicked("7")
            R.id.btn8 -> numberBtnClicked("8")
            R.id.btn9 -> numberBtnClicked("9")
            R.id.btnPlus -> operateBtnClicked("+")
            R.id.btnMinus -> operateBtnClicked("-")
            R.id.btnMulti -> operateBtnClicked("*")
            R.id.btnDivide -> operateBtnClicked("/")
            R.id.btnModulo -> operateBtnClicked("%")
        }
    }

    private fun numberBtnClicked(number: String) {

        if (isOperator) {
            binding.tvExpression.append(" ")
        }
        isOperator = false

        val expressionText = binding.tvExpression.text.split(" ")

        if (expressionText.last().length >= 15) {
            Toast.makeText(this, "15자리 까지만 사용할 수 있습니다.", Toast.LENGTH_SHORT).show()
        } else if (expressionText.last().isEmpty() && number == "0") {
            Toast.makeText(this, "0은 제일 앞에 올 수 없습니다.", Toast.LENGTH_SHORT).show()
        }
        binding.tvExpression.append(number)
        binding.tvResult.text = calculateExpression()
    }

    private fun operateBtnClicked(op: String) {
        if (binding.tvExpression.text.isEmpty()) {
            return
        }

        when {
            isOperator -> {
                val text = binding.tvExpression.text.toString()
                binding.tvExpression.text = text.dropLast(1) + op
            }
            hasOperator -> {
                Toast.makeText(this, "연산자는 한 번만 사용 가능합니다.", Toast.LENGTH_SHORT).show()
                return
            }
            else -> {
                binding.tvExpression.append(" $op")
            }
        }

        val ssb = SpannableStringBuilder(binding.tvExpression.text)
        ssb.setSpan(
            ForegroundColorSpan(getColor(R.color.green)),
            binding.tvExpression.length() - 1,
            binding.tvExpression.length(),
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        binding.tvExpression.text = ssb

        isOperator = true
        hasOperator = true
    }


    fun resultBtnClicked(view: View) {
        val expressionText = binding.tvExpression.text.toString()
        val resultText = calculateExpression()

        if (resultText.isEmpty()) {
            Toast.makeText(this, "수식을 확인하세요", Toast.LENGTH_SHORT).show()
            return
        }

        Thread(Runnable {
            db.historyDao().insertHistory(History(null, expressionText, resultText))
        }).start()

        binding.tvResult.text = ""
        binding.tvExpression.text = resultText

        isOperator = false
        hasOperator = false
    }

    private fun calculateExpression(): String {
        val expressionTexts = binding.tvExpression.text.split(" ")

        if (hasOperator.not() || expressionTexts.size != 3) {
            return ""
        } else if (expressionTexts[0].isNumber().not() || expressionTexts[2].isNumber().not()) {
            return ""
        }

        val exp1 = expressionTexts[0].toBigInteger()
        val exp2 = expressionTexts[2].toBigInteger()
        val op = expressionTexts[1]

        return when (op) {
            "+" -> (exp1 + exp2).toString()
            "-" -> (exp1 - exp2).toString()
            "/" -> (exp1 / exp2).toString()
            "*" -> (exp1 * exp2).toString()
            "%" -> (exp1 % exp2).toString()
            else -> ""
        }
    }

    private fun String.isNumber(): Boolean {
        return try {
            this.toBigInteger()
            true
        } catch (e: NumberFormatException) {
            false
        }
    }

    fun clearBtnClicked(view: View) {
        binding.tvExpression.text = ""
        binding.tvResult.text = ""
        isOperator = false
        hasOperator = false
    }

    fun historyBtnClicked(view: View) {
        binding.layoutHistory.visibility = View.VISIBLE

        Thread(Runnable {
            val historyList = db.historyDao().getAll()
            historyAdapter.setItem(historyList)
            runOnUiThread {
                historyAdapter.notifyDataSetChanged()
            }
        }).start()
    }

    fun clearHistoryBtnClicked(view: View) {
        Thread(Runnable {
            db.historyDao().deleteAll()
            historyAdapter.clearItem()
            runOnUiThread {
                historyAdapter.notifyDataSetChanged()
            }
        }).start()
    }

    fun closeHistoryBtnClicked(view: View) {
        binding.layoutHistory.visibility = View.GONE
    }
}