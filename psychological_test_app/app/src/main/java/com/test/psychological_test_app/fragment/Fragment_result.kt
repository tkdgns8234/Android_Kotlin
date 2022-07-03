package com.test.psychological_test_app.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.test.psychological_test_app.R
import com.test.psychological_test_app.TestResult
import com.test.psychological_test_app.databinding.FragmentResultBinding

class Fragment_result : Fragment() {
    private lateinit var mBinding :FragmentResultBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mBinding = FragmentResultBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //bundle 객체를 받아올 땐 arguments 키워드 사용
        // 엘비스 연산자 ?: Null 일 경우의 동작
        var option = arguments?.getInt(Fragment_selection.INDEX) ?: -1
        setResult(option)

        mBinding.ivResultHome.setOnClickListener(View.OnClickListener {
            view.findNavController().navigate(R.id.action_fragment_result_to_fragment_main)
        })
    }

    private fun setResult(option: Int) {
        when(option) {
            1 -> {
                mBinding.tvResultMain.text = TestResult.RESULT_MAIN_1
                mBinding.tvResultSub.text = TestResult.RESULT_SUB_1
            }
            2 -> {
                mBinding.tvResultMain.text = TestResult.RESULT_MAIN_2
                mBinding.tvResultSub.text = TestResult.RESULT_SUB_2
            }
            3 -> {
                mBinding.tvResultMain.text = TestResult.RESULT_MAIN_3
                mBinding.tvResultSub.text = TestResult.RESULT_SUB_3
            }
            4 -> {
                mBinding.tvResultMain.text = TestResult.RESULT_MAIN_4
                mBinding.tvResultSub.text = TestResult.RESULT_SUB_4
            }
            else -> return
        }
    }

}