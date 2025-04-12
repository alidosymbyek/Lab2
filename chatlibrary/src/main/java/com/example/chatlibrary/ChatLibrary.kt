package com.example.chatlibrary

import android.content.Context
import android.content.Intent

object ChatLibrary {
    fun start(context: Context) {
        val intent = Intent(context, ChatActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }
} 