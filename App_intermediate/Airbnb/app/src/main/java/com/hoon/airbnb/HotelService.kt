package com.hoon.airbnb

import retrofit2.Call
import retrofit2.http.GET

interface HotelService {
    @GET("v3/8117310a-c5ea-41bf-973e-8d6cafc841bb")
    fun getHotelList(): Call<HotelDTO>
}