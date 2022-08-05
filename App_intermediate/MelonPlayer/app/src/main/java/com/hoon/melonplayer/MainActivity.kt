package com.hoon.melonplayer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.hoon.melonplayer.databinding.ActivityMainBinding

/*
Listadapter를 사용한 recyclerView의 item 업데이트 과정에서 또 엄청나게 해맸다.
이전에 동일한 문제가 있었기에 동일한 방법으로 해결하려 했으나 실패,

문제 원인은 아래와 같다

submitList() 를 통해 등록된 recyclerview의 아이템이 reference type인 경우
해당 reference 의 값을 변경하면 recyclerview에 등록된 item의 값도 바뀌기 때문에 (값의 참조이기 때문)
diffutil 클래스를 통한 비교 시, 동일한 값으로 처리되어 아이템이 업데이트 되지 않았었다.

참고할 부분: PlayerFragment 의 updatePlayListView()
  - (model 클래스를 새로 생성하여 대입)


다신 recyclerview에서 헤메지 않으리..!
 */

class MainActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, PlayerFragment.newInstance())
            .commit()
    }
}