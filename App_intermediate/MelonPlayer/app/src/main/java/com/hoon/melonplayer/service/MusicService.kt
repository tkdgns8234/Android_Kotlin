package com.hoon.melonplayer.service

import retrofit2.Call
import retrofit2.http.GET

interface MusicService {
    @GET("/v3/19b14603-d076-40ba-a7db-fe2de9725f02")
    fun getMusicList() : Call<MusicDTO>
}