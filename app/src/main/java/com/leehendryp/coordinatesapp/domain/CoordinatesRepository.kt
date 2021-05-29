package com.leehendryp.coordinatesapp.domain

import com.leehendryp.coordinatesapp.data.local.Coordinates

interface CoordinatesRepository {
    suspend fun getCoordinateEntries(): List<Coordinates>
    suspend fun save(coordinates: Coordinates): Boolean
}