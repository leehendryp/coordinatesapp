package com.leehendryp.coordinatesapp.domain

import com.google.gson.annotations.SerializedName

data class Coordinates(
    @SerializedName("latitude") val latitude: Double,
    @SerializedName("longitude") val longitude: Double
) {
    override fun toString() = "($latitude, $longitude)"
}