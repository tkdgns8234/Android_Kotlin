package com.example.simplegallery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.simplegallery.databinding.FragmentMainBinding

class FragmentMain : Fragment() {
    var _binding : FragmentMainBinding? = null
    val binding :FragmentMainBinding get() { return _binding!! }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.iv1.setOnClickListener(CustomOnClickListener())
        binding.iv2.setOnClickListener(CustomOnClickListener())
        binding.iv3.setOnClickListener(CustomOnClickListener())
        binding.iv4.setOnClickListener(CustomOnClickListener())
        binding.iv5.setOnClickListener(CustomOnClickListener())
        binding.iv6.setOnClickListener(CustomOnClickListener())
        binding.iv7.setOnClickListener(CustomOnClickListener())
    }

    inner class CustomOnClickListener : View.OnClickListener {
        override fun onClick(v: View) {
            // tag 가 숫자이고 1~7 사이의 숫자인 경우 resource id 전달
            val isNumeric = v.tag.toString().toIntOrNull()
            if (isNumeric != null && isNumeric in 1..7) {
                val name = "wyj" + v.tag
                val did = resources.getIdentifier(name, "drawable", activity?.packageName)
                val action = FragmentMainDirections.actionFragmentMainToFragmentDetail(did)
                v.findNavController().navigate(action)
            }
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}