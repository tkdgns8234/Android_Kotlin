package com.test.psychological_test_app.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.test.psychological_test_app.R
import com.test.psychological_test_app.databinding.FragmentResultBinding
import com.test.psychological_test_app.databinding.FragmentSelectionBinding

class Fragment_selection : Fragment(), View.OnClickListener {
    private var _mbinding: FragmentSelectionBinding? = null
    private val mBinding get() = _mbinding!!

    private lateinit var mNavController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _mbinding = FragmentSelectionBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mNavController = view.findNavController()

        mBinding.ivSelectionBack.setOnClickListener(this)
        mBinding.tvSelection1.setOnClickListener(this)
        mBinding.tvSelection2.setOnClickListener(this)
        mBinding.tvSelection3.setOnClickListener(this)
        mBinding.tvSelection4.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.tv_selection_1 -> {NavigateWithIndex(1)}
            R.id.tv_selection_2 -> {NavigateWithIndex(2)}
            R.id.tv_selection_3 -> {NavigateWithIndex(3)}
            R.id.tv_selection_4 -> {NavigateWithIndex(4)}
            R.id.iv_selection_back -> {
                // 백스택의 최상단 item pop (현재 fragment 를 pop)
                mNavController.popBackStack()
            }
        }
    }

    private fun NavigateWithIndex(idx: Int) {
        // TODO 아래 bundle 관련 문법이 생소해 관련해서 찾아보자
        var bundle = bundleOf(INDEX to idx)
        mNavController.navigate(R.id.action_fragment_selection_to_fragment_result, bundle)
    }

    // 자바에서 static 변수를 활용할때처럼 사용 가능
    companion object {
        // val 은 그 자체로 상수인줄 알았는데 const?
        // const val -> 컴파일타임에 그 값이 결정되어야함을 의미 -> primitive 타입 데이터만 사용 가능
        // val ->  runtime에 결정됨  e.g) class 의 인스턴스 대입 가능

        const val INDEX = "index"
    }

    override fun onDestroy() {
        _mbinding = null
        super.onDestroy()
    }
}