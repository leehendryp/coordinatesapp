package com.leehendryp.coordinatesapp.domain

interface CoordinatesRepository {
    suspend fun getCoordinateEntries(): List<Coordinates>
    suspend fun save(coordinates: Coordinates): Boolean
}