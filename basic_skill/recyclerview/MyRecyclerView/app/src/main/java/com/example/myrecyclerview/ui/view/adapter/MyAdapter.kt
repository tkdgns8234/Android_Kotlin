package com.example.myrecyclerview.ui.view.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myrecyclerview.R
import com.example.myrecyclerview.data.Profile
import com.example.myrecyclerview.databinding.LayoutMyrecyclerviewItemBinding

class MyAdapter(private val profileList: ArrayList<Profile>) : RecyclerView.Adapter<MyAdapter.MyViewHolder>() {
    private var myOnClickListener : MyOnClickListener? = null

    fun setMyOnClickListener(listener: MyOnClickListener) {
        myOnClickListener = listener
    }

    interface MyOnClickListener {
        fun onClick(position: Int)
    }

    class MyViewHolder(val binding: LayoutMyrecyclerviewItemBinding) :RecyclerView.ViewHolder(binding.root){
        private var profileImage = binding.ivProfile
        private var profileName = binding.tvName
        private var profileNumber = binding.tvNumber

        fun bind (profile: Profile) {
            profileName.text = profile.name
            profileNumber.text = profile.number.toString()

            Glide
                .with(binding.root)
                .load(profile.imageURL)
                .centerCrop()
                .placeholder(R.mipmap.ic_launcher) // 기본 이미지 설정
                .into(profileImage);
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        // inflate 시킬 때 두 번째 파라미터로 parent viewGroup을 설정해야 현재 inflate 시키는 viewGroup의 match_parent 같은 속성이 적용됨
        val binding = LayoutMyrecyclerviewItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.binding.root.setOnClickListener(View.OnClickListener {
            myOnClickListener?.onClick(position)
        })
        holder.bind(profileList.get(position))
    }

    override fun getItemCount(): Int {
        return profileList.size
    }
}