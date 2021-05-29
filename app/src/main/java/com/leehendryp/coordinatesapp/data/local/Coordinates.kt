package com.leehendryp.coordinatesapp.data.local

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "coord_table")
data class Coordinates(
    @PrimaryKey(autoGenerate = true) val id: Int? = 0,
    @NonNull @ColumnInfo(name = "date") val date: String,
    @NonNull @ColumnInfo(name = "latitude") val latitude: Double,
    @NonNull @ColumnInfo(name = "longitude") val longitude: Double,
) {
    override fun toString() = "($latitude, $longitude)"
}