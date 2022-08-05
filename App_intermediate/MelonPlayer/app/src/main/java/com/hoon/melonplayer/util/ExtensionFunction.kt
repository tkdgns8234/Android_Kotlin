package com.hoon.melonplayer.util

import com.hoon.melonplayer.model.MusicModel
import com.hoon.melonplayer.service.MusicEntity

// musicModel 클래스와 MusicEntity의 분리
// MusicEntity class -> 서버 데이터를 그대로 모두 받아온 class
// MusicModel class -> 현재 앱의 music list에서 사용할 model class

fun MusicEntity.mapper(id: Int): MusicModel {
    return MusicModel(
        id = id,
        title = this.title,
        artist = this.artist,
        musicURL = this.musicURL,
        coverImageURL = this.coverImageURL,
    )
}