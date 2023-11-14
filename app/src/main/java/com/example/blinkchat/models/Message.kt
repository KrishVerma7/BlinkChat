package com.example.blinkchat.models

import android.content.Context
import com.example.blinkchat.utils.formatAsHeader
import java.util.Date

interface ChatEvent {
    val sentAt: Date
}

data class Message(
    val msg: String,
    val senderId: String,
    val msgId: String,
    val type: String = "TEXT",
    val status: Int = 1,
    val liked: Boolean = false,
    override val sentAt: Date = Date()
) : ChatEvent {
    constructor():this("","","","",1,false,Date())
}

data class DataHeader(
    override val sentAt: Date = Date(),val context: Context
):ChatEvent{
    val data:String = sentAt.formatAsHeader(context)
}