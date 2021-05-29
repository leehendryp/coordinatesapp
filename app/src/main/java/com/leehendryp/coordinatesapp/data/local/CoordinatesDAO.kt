package com.leehendryp.coordinatesapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CoordinatesDAO {
    @Query("SELECT * FROM coord_table")
    suspend fun getAll(): List<Coordinates>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(coordinates: Coordinates)
}