package com.example.cartapp.data.common.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object Converters {
    private val gson = Gson()

    @TypeConverter
    @JvmStatic
    fun listToJson(list: List<String>?): String =
        gson.toJson(list)

    @TypeConverter
    @JvmStatic
    fun jsonToList(json: String?): List<String> =
        json
            ?.let {
                val type = object : TypeToken<List<String>>() {}.type
                gson.fromJson(it, type)
            }
            ?: emptyList()
}