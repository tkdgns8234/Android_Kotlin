package com.hoon.body_calendar.ui.view

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.icu.util.Calendar
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.DatePicker
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.hoon.body_calendar.R
import com.hoon.body_calendar.databinding.ActivityMainBinding
import com.hoon.body_calendar.model.Memo
import com.hoon.body_calendar.ui.adapter.MyListViewAdapter
import com.hoon.body_calendar.util.Constants


/*
TODO:
 1. list view를 Recycler view로 변경
 2. view 위치 변경, 삭제 등 기능 추가 (참고 자료 https://www.youtube.com/watch?v=zNGVicOZ2ew)
 */

class MainActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    private val databaseUserRef: DatabaseReference by lazy {
        Firebase.database.reference.child(Constants.DB_APP_NAME).child(Constants.DB_TABLE_USER)
            .child(Firebase.auth.currentUser!!.uid)
    }
    lateinit var myListViewAdapter : MyListViewAdapter
    private val momoList = mutableListOf<Memo>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setListView()
        setDataBase()
        binding.floatingBtnCalendar.setOnClickListener { showCustomDialog() }
    }

    private fun setDataBase() {
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                momoList.clear()
                // firebase 데이터 베이스에서 데이터 load
                for (data in dataSnapshot.children) {
                    Log.e(TAG, data.getValue(Memo::class.java).toString())
                    momoList.add(data.getValue(Memo::class.java)!!)
                }
                myListViewAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.d(TAG, "loadPost:onCancelled", databaseError.toException())
            }
        }
        databaseUserRef.addValueEventListener(postListener)
    }

    private fun setListView() {
        myListViewAdapter = MyListViewAdapter(momoList)
        binding.listViewMain.adapter = myListViewAdapter
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
            val myRef = databaseUserRef
            val date = btnSelectDate.text.toString()
            val memo = etMemo.text.toString()
            Log.e(TAG, "val = $date$memo")

            if (date == resources.getString(R.string.select_date) || memo == "") {
                Toast.makeText(applicationContext, "날짜 또는 메모를 입력해주세요", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // push() 사용 시 동일한 테이블 참조해도 중복 저장됨
            myRef.push().setValue(Memo(date, memo))
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