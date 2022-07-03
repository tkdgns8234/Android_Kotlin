package com.test.psychological_test_app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import com.test.psychological_test_app.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    // late init 을 사용하는 경우 요소 선언 후 null 값을 대입하지 않아도 된다.
    // null 값을 대입하는 경우 ? 를 추가하여 선언해야하고 noNull 타입에 대입이 안되기 때문에 귀찮은 작업들이 뒤따른다.
    private lateinit var mBinding : ActivityMainBinding
    private lateinit var mNavController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        // 현재 main activity 에서 mNavController를 사용하지 않음
//        val navHostFragment =
//            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
//        mNavController = navHostFragment.navController
    }
}