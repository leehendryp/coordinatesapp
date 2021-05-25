package com.leehendryp.coordinatesapp.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [RoomCoordinates::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class CoordinatesRoomDatabase : RoomDatabase() {

    abstract fun coordinatesDao(): CoordinatesDAO

    companion object {
        private const val DATABASE = "coord_database"

        @Volatile
        private var INSTANCE: CoordinatesRoomDatabase? = null

        fun getDatabase(context: Context): CoordinatesRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CoordinatesRoomDatabase::class.java,
                    DATABASE
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}