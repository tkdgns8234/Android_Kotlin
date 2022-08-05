package com.hoon.melonplayer.model

data class MusicModel (
    val id: Int,
    val title: String,
    val artist: String,
    val musicURL: String,
    val coverImageURL: String,
    var isPlayingStatus: Boolean = false
)