package com.leehendryp.coordinatesapp.data.local

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {
    @TypeConverter
    fun fromCoordinates(coordinates: Coordinates): String {
        val type = object : TypeToken<Coordinates>() {}.type
        return Gson().toJson(coordinates, type)
    }

    @TypeConverter
    fun toCoordinates(string: String): Coordinates {
        val type = object : TypeToken<Coordinates>() {}.type
        return Gson().fromJson(string, type)
    }
}