package com.example.myrecyclerview.ui.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myrecyclerview.data.Profile
import com.example.myrecyclerview.databinding.ActivityMainBinding
import com.example.myrecyclerview.ui.view.adapter.MyAdapter

class MainActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initRecyclerView()
    }

    private fun initRecyclerView() {
        val profileList = ArrayList<Profile>()
        for (i in 1..10) {
            profileList.add(Profile("정상훈", i))
        }

        val myAdapter : MyAdapter = MyAdapter(profileList)
        val myRecyclerView = binding.recyclerView
        myRecyclerView.apply {
            adapter = myAdapter
            layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL, false)
        }

        myAdapter.setMyOnClickListener(object :MyAdapter.MyOnClickListener {
            override fun onClick(position: Int) {
                Toast.makeText(applicationContext, "정상훈 + ${position+1}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}

//https://dimg.donga.com/wps/NEWS/IMAGE/2022/01/28/111500268.2.jpg