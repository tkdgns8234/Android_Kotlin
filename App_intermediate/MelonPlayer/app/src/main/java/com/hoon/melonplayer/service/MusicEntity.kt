package com.hoon.melonplayer.service

import com.google.gson.annotations.SerializedName

data class MusicEntity(
    @SerializedName("title") val title: String,
    @SerializedName("artist") val artist: String,
    @SerializedName("musicURL") val musicURL: String,
    @SerializedName("coverImageURL") val coverImageURL: String
)
