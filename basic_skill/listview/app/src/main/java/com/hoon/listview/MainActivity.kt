package com.hoon.listview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.google.android.material.snackbar.Snackbar
import com.hoon.listview.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.main = this

        initListView()
    }

    private fun initListView() {
        val customList = mutableListOf<CustomModel>()
        customList.add(CustomModel("정상훈", "테스트"))
        customList.add(CustomModel("김성원", "테스트"))
        customList.add(CustomModel("홍순재", "테스트"))
        customList.add(CustomModel("가나다", "테스트가나다"))

        with(binding.listView) {
            adapter = CustomListAdapter(customList)
        }
    }

    fun onClick() {
        Snackbar.make(binding.root, "test", Snackbar.LENGTH_SHORT).show()
    }
}