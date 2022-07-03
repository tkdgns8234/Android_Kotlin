package com.test.psychological_test_app.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.test.psychological_test_app.R
import com.test.psychological_test_app.databinding.FragmentMainBinding
import com.test.psychological_test_app.databinding.FragmentQuestionBinding

class Fragment_question : Fragment() {
    private var _mbinding: FragmentQuestionBinding? = null
    private val mBinding get() = _mbinding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _mbinding = FragmentQuestionBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mBinding.ivQuestionNext.setOnClickListener {
            //navigation 하는 또 다른 방법 (동작은 같음)
            view.findNavController().navigate(R.id.action_fragment_question_to_fragment_selection)
        }
    }

    override fun onDestroy() {
        _mbinding = null
        super.onDestroy()
    }
}