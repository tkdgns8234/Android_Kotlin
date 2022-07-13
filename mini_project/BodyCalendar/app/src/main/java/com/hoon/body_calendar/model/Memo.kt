package com.hoon.body_calendar.model

import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class Memo(val date : String="", val memo : String="", var ID: Long? = null) {

    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "date" to date,
            "memo" to memo,
        )
    }
}