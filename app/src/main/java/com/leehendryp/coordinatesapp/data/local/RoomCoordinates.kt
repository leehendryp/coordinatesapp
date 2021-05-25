package com.leehendryp.coordinatesapp.data.local

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.leehendryp.coordinatesapp.domain.Coordinates

@Entity(tableName = "coord_table")
class RoomCoordinates(
    @PrimaryKey(autoGenerate = true) val id: Int? = 0,
    @NonNull
    @TypeConverters(Converters::class)
    @ColumnInfo(name = "coordinates") val coordinates: Coordinates
)