package com.leehendryp.coordinatesapp.data

import androidx.annotation.WorkerThread
import com.leehendryp.coordinatesapp.data.local.Coordinates
import com.leehendryp.coordinatesapp.data.local.CoordinatesDAO
import com.leehendryp.coordinatesapp.domain.CoordinatesRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CoordinatesRepositoryImpl(
    private val coordinatesDAO: CoordinatesDAO,
    private val coroutineDispatcher: CoroutineDispatcher = Dispatchers.IO
) : CoordinatesRepository {
    override suspend fun getCoordinateEntries(): List<Coordinates> {
        return try {
            with(coroutineDispatcher) {
                coordinatesDAO.getAll()
            }
        } catch (throwable: Throwable) {
            throw throwable
        }
    }

    @WorkerThread
    override suspend fun save(coordinates: Coordinates): Boolean {
        return try {
            withContext(coroutineDispatcher) {
                coordinatesDAO.insert(coordinates)
            }
            true
        } catch (throwable: Throwable) {
            false
        }
    }
}