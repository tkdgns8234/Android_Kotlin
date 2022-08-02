package com.hoon.airbnb

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.hoon.airbnb.databinding.ActivityMainBinding
import com.hoon.airbnb.databinding.DialogBottomSheetBinding
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.*
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.Overlay
import com.naver.maps.map.util.FusedLocationSource
import com.naver.maps.map.util.MarkerIcons
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/*
 xml의 fragment 태그는 viewBinding 으로 직접 접근이 안됨,
 android:name 속성으로 fragment 클래스를 지정해놓으면 해당 클래스가 가장 먼저 인스턴스화 됨
 - fragmentManager의 findFragmentById 로 접근

 retrofit model 클래스의 key와 서버에서 response 하는 json key 값이 다르면 데이터를 못가져온다.
 이것때문에 괜히 한참 삽질했다. 다음부턴 json to kotlin data class 플러그인을 사용하자..

 viewPager2 -> 프래그먼트, 뷰 전환에 주로 사용됨
 */

class MainActivity : AppCompatActivity(), OnMapReadyCallback {
    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    private lateinit var naverMap: NaverMap
    private lateinit var locationSource: FusedLocationSource
    private val recyclerViewAdapter = HotelListAdapter()
    private val viewPagerAdapter = HotelViewPagerAdapter(onClick = { hotel ->
        //action send, MimeType을 text로 하는 intent filter의 앱 리스트를 chooser 형태로 visible
        val intent = Intent()
            .apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, "지금 이 가격에 예약하세요! ${hotel.title} 가격: ${hotel.price} 이미지 보기: ${hotel.imgUrl}")
                type = "text/plain"
            }
        startActivity(Intent.createChooser(intent, null))
    })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        requestMapReference()
        initVies()
    }

    private fun initVies() {
        binding.hotelViewPager.adapter = viewPagerAdapter
        binding.hotelViewPager.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)

                val hotel = viewPagerAdapter.currentList[position]
                val cameraUpdate = CameraUpdate.scrollTo(LatLng(hotel.lat, hotel.lng))
                naverMap.moveCamera(cameraUpdate)
            }
        })
        binding.dialogBottomSheet.rvHotelList.adapter = recyclerViewAdapter
        binding.dialogBottomSheet.rvHotelList.layoutManager = LinearLayoutManager(this)
    }

    private fun requestMapReference() {
        val fm = supportFragmentManager
        val mapFragment = fm.findFragmentById(R.id.map) as MapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(map: NaverMap) {
        naverMap = map.also {
            it.minZoom = 16.0
            it.maxZoom = 21.0
        }

        // 카메라 이동, 강남역 좌표
        val cameraUpdate = CameraUpdate.scrollTo(LatLng(37.498078227741296, 127.0280002723799))
        naverMap.moveCamera(cameraUpdate)

        // 현재 위치 표시 버튼
        binding.LocationButtonView.map = naverMap

        requestLocationPermission()
        getHotelListFromAPI()
    }

    private fun requestLocationPermission() {
        // FusedLocationSource 클래스는 위치 권한 정보와 관련된 로직을 포함함, onRequestPermissionResult 호출됨
        // 참조: https://navermaps.github.io/android-map-sdk/guide-ko/4-2.html
        locationSource = FusedLocationSource(this@MainActivity, LOCATION_PERMISSION_REQUSET_CODE)
        naverMap.locationSource = locationSource
    }

    private fun getHotelListFromAPI() {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://run.mocky.io")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(HotelService::class.java).also {
            it.getHotelList().enqueue(object : Callback<HotelDTO> {
                override fun onResponse(call: Call<HotelDTO>, response: Response<HotelDTO>) {
                    if (response.isSuccessful.not()) {
                        // 실패
                        return
                    }

                    response.body()?.let { hotelDTO ->
                        updateMarkers(hotelDTO.items)
                        viewPagerAdapter.submitList(hotelDTO.items.toList())
                        recyclerViewAdapter.submitList(hotelDTO.items.toList())
                        binding.dialogBottomSheet.tvBottomSheetTitle.text = "${hotelDTO.items.size}개의 숙소"
                    }
                }

                override fun onFailure(call: Call<HotelDTO>, t: Throwable) {}
            })
        }
    }

    private fun updateMarkers(hotelList: List<Hotel>) {
        hotelList.forEach { hotel ->
            val marker = Marker()
            marker.position = LatLng(hotel.lat, hotel.lng)
            marker.map = naverMap
            marker.tag = hotel.id
            marker.icon = MarkerIcons.BLACK
            marker.iconTintColor = Color.BLUE
            // 마커 클릭 리스너
            marker.onClickListener = markerOnClickListener
        }
    }

    private val markerOnClickListener =
    // marker의 tag 와 hotel model 클래스의 id가 동일한것을 찾고 viewpager를 이동시킨다.
        // 탐색을 한싸이클O(n)에 그치기 위해 withIndex 사용
        Overlay.OnClickListener { overlay ->
            val index = viewPagerAdapter.currentList.withIndex() // index를 포함한 iterator 반환
                .find {
                    it.value.id == overlay.tag
                }
                ?.index ?: -1

            binding.hotelViewPager.currentItem = index

            return@OnClickListener true // true 로 return 하는 경우 처리 완료를 의미
        }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (locationSource.onRequestPermissionsResult(requestCode, permissions, grantResults)) {
            if (!locationSource.isActivated()) { // 권한 거부됨
                naverMap.setLocationTrackingMode(LocationTrackingMode.None);
            }
            return;
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUSET_CODE = 1000
    }
}