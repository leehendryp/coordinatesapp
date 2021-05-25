package com.leehendryp.coordinatesapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CoordinatesDAO {
    @Query("SELECT * FROM coord_table")
    fun getAll(): List<RoomCoordinates>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(roomCoordinates: RoomCoordinates)
}