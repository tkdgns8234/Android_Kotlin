package com.hoon.airbnb.model

data class Hotel(
    val id: Int,
    val imgUrl: String,
    val lat: Double,
    val lng: Double,
    val price: String,
    val title: String
)