package com.hoon.body_calendar.model

import android.util.Log
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.hoon.body_calendar.util.Constants

class RealtimeDatabase {
    private val databaseUserRef: DatabaseReference by lazy {
        Firebase.database.reference.child(Constants.DB_APP_NAME).child(Constants.DB_TABLE_USER)
            .child(Firebase.auth.currentUser!!.uid)
    }
    private lateinit var listener: OnDataChangeListener

    interface OnDataChangeListener {
        fun onDataChange(momoList: MutableList<Memo>)
    }

    fun setOnDataChangeListener(_listener: OnDataChangeListener) {
        listener = _listener
    }

    init {
        initDataBase()
    }

    private fun initDataBase() {
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var memoList = mutableListOf<Memo>()
                memoList.clear()
                // firebase 데이터 베이스에서 데이터 load
                for (data in dataSnapshot.children) {
                    if (data.key != Constants.DB_MEMO_ID) {
                        memoList.add(data.getValue(Memo::class.java)!!)
                        memoList.get(memoList.size - 1).ID = data.key?.substring(2)?.toLong()
                    }
                }

                listener?.let { it.onDataChange(memoList) }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.d(TAG, "loadPost:onCancelled", databaseError.toException())
            }
        }
        databaseUserRef.addValueEventListener(postListener)
    }

    fun writeData(date: String, memo: String) {
        // 현재 user에 대한 memo id를 서버에 요청해서 가져오면
        // 해당 id로 새로운 memo 데이터를 만든다.ㄴ
        databaseUserRef.child(Constants.DB_MEMO_ID).get()
            .addOnSuccessListener {
                var nextID =
                    when (val id = it.getValue(Long::class.java)) {
                        null -> 0L
                        else -> id + 1
                    }

                val memo = Memo(date, memo, nextID)
                val postValues = memo.toMap()

                val childUpdates = hashMapOf<String, Any>()
                childUpdates.put("${Constants.DB_MEMO_ID + nextID}", postValues)
                databaseUserRef.updateChildren(childUpdates)

                setDBUserID(nextID)
            }
    }

    fun removeData(id: Long) {
        databaseUserRef.child("${Constants.DB_MEMO_ID + id}").setValue(null)
    }

    private fun setDBUserID(id: Long) {
        databaseUserRef.child(Constants.DB_MEMO_ID).setValue(id)
    }

    companion object {
        const val TAG = "RealtimeDatabase"
    }
}