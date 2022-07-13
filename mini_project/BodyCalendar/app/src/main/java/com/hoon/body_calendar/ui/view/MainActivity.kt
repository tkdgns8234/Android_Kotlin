package com.hoon.body_calendar.ui.view

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.icu.util.Calendar
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.hoon.body_calendar.R
import com.hoon.body_calendar.databinding.ActivityMainBinding
import com.hoon.body_calendar.model.Memo
import com.hoon.body_calendar.model.RealtimeDatabase
import com.hoon.body_calendar.ui.adapter.MyRecyclerViewAdapter
import com.hoon.body_calendar.ui.callback.MyItemTouchHelperCallback

/*
TODO:
 1. list view를 Recycler view로 변경
   -> 완료, listAdapter를 상속하도록 하여 item 업데이트를 효율적으로 하도록 도와주는 diff utils 적용
   데이터 업데이트방법: submitList()
 2. 리사이클러뷰의 item 위치 변경 및 삭제 기능 추가 (참고 자료 https://www.youtube.com/watch?v=zNGVicOZ2ew)
   -> 완료, 리사이클러뷰의 item touch callback 적용
 3. 유지보수성 향상을 위해 Database를 model 패키지로 빼서 따로 관리
   -> 완료
 4. 앱에서 메모 순서를 변경했을 때 파이어베이스 DB의 데이터 위치도 동일하게 정렬시키기(메모의 ID값 이용)
 */

class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val rDB by lazy { RealtimeDatabase() }

    lateinit var myViewAdapter : MyRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setDataBase()
        setListView()
        binding.floatingBtnCalendar.setOnClickListener { showCustomDialog() }
    }

    private fun setDataBase() {
        rDB.setOnDataChangeListener(object : RealtimeDatabase.OnDataChangeListener {
            override fun onDataChange(memoList: MutableList<Memo>) {
                myViewAdapter.submitList(memoList)
            }
        })
    }

    private fun setListView() {
        myViewAdapter = MyRecyclerViewAdapter()
        with(binding.recyclerViewMain) {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = myViewAdapter
        }

        val itemTouchHelperCallback = ItemTouchHelper(MyItemTouchHelperCallback(binding.recyclerViewMain, rDB))
        itemTouchHelperCallback.attachToRecyclerView(binding.recyclerViewMain)
    }

    private fun showCustomDialog() {
        val customDialog = AlertDialog.Builder(this)
            .setView(R.layout.layout_dialog_calendar)
            .show()

        val etMemo = customDialog.findViewById<TextView>(R.id.et_dialog_memo)
        val btnSelectDate = customDialog.findViewById<Button>(R.id.btn_dialog_selectDate)
        val btnSave = customDialog.findViewById<Button>(R.id.btn_dialog_save)

        btnSelectDate.setOnClickListener {
            showDatePickerDialog(btnSelectDate)
        }

        btnSave.setOnClickListener {
            // save data to realtime database !
            val date = btnSelectDate.text.toString()
            val memo = etMemo.text.toString()
            Log.e(TAG, "val = $date$memo")

            if (date == resources.getString(R.string.select_date) || memo == "") {
                Toast.makeText(applicationContext, "날짜 또는 메모를 입력해주세요", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // push() 사용 시 동일한 테이블 참조해도 중복 저장됨
            rDB.writeData(date, memo)
            customDialog.dismiss()
        }
    }

    private fun showDatePickerDialog(btnSelectDate: Button) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        DatePickerDialog(this, object : DatePickerDialog.OnDateSetListener {
            override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
                btnSelectDate.text = "$year, $month, $dayOfMonth"
            }
        }, year, month, day).show()
    }

    companion object {
        const val TAG = "MainActivity"
    }
}