package com.example.pgr208.models

import android.content.Context
import android.content.SharedPreferences

class SettingDTO(context: Context) {
    val myPreference: SharedPreferences = context.getSharedPreferences("mySettings", Context.MODE_PRIVATE)
}