package com.hoon.melonplayer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.hoon.melonplayer.databinding.ActivityMainBinding

/*
recyclerView의 Listadapter -> submitList가 또 매우 골머리 아프게 했다..
이건 구글 sdk 구현이 좀 이상한거같아 의도한 동작이 이게 맞나;?
submitList 로 List 데이터를 넘길 때 List 만 참조를 바꿔서 넣으면 업데이트가 잘 될거라 생각했다.
이전에 동일하게 list 업데이트 문제가 있었기 때문에 하지만 아니었다.
List 내부의 Item이 reference 타입인 경우 -> 외부에서 해당 Item 값을 변경할 경우 difutil에 등록된 값도 변경되므로
이에 유의하여 새로운 아이템을 생성해서 submitList를 해주도록 하자
참고할 부분: PlayerFragment 의 updatePlayListView()

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