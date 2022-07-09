package com.example.simplegallery

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.simplegallery.databinding.FragmentDetailBinding

class FragmentDetail : Fragment() {
    private var _binding : FragmentDetailBinding? = null
    private val binding : FragmentDetailBinding get() { return _binding!! }
    private val args: FragmentDetailArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("test", args.resourceId.toString())
        binding.ivDetail.setImageResource(args.resourceId)
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}