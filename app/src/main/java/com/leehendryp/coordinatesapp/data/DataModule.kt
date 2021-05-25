package com.leehendryp.coordinatesapp.data

import android.content.Context
import com.leehendryp.coordinatesapp.data.local.CoordinatesDAO
import com.leehendryp.coordinatesapp.data.local.CoordinatesRoomDatabase
import com.leehendryp.coordinatesapp.data.local.CoordinatesRoomDatabase.Companion.getDatabase
import com.leehendryp.coordinatesapp.domain.CoordinatesRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton
import kotlinx.coroutines.Dispatchers

@Module
class DataModule {
    @Singleton
    @Provides
    fun provideRoomDatabase(context: Context): CoordinatesRoomDatabase = getDatabase(context)

    @Singleton
    @Provides
    fun provideRoomDao(database: CoordinatesRoomDatabase): CoordinatesDAO =
        database.coordinatesDao()

    @Singleton
    @Provides
    fun provideRepository(dao: CoordinatesDAO): CoordinatesRepository =
        CoordinatesRepositoryImpl(dao, Dispatchers.IO)
}