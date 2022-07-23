package com.test.psychological_test_app.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.test.psychological_test_app.R
import com.test.psychological_test_app.databinding.FragmentMainBinding

/*
*
* navigation 사용 시
* 아래 두 속성을 true 로 줘서 back stack을 관리해야함에 유의
* app:popUpTo="@id/fragment_main"    : 이동 위치보다 위에있는 스택을 모두 pop
* app:popUpToInclusive="true" />     : 이동하려는 위치의 fragment 스택을 push 하지않음
 */

class Fragment_main : Fragment() {
    private var _mbinding: FragmentMainBinding? = null
    private val mBinding get() = _mbinding!!

//    private lateinit var mBinding: FragmentMainBinding
    private lateinit var mNavController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // 뷰 바인딩
        // 액티비티 와는 다르게 layoutInflater 를 쓰지 않고 inflater 인자를 가져와 뷰와 연결한다.
        // (사실 두개는 동일한객체임, 시스템에서 main activity 의 inflater 를 전달해주는것으로 보임)
        _mbinding = FragmentMainBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //NavController 초기화
        mNavController = Navigation.findNavController(view)

        // 아래 문장을 뜯어보면 setOnClickListener에 인터페이스 함수를 람다식으로 전달하는 구문
        // 전달 인자가 람다 하나기때문에 괄호 () 생략 후 {} (본문)작성
        mBinding.ivMainNext.setOnClickListener {
            mNavController.navigate(R.id.action_fragment_main_to_fragment_question)
        }

        println("back stack cnt = " + parentFragmentManager.backStackEntryCount)
    }

    override fun onDestroy() {
        _mbinding = null
        super.onDestroy()
    }
}