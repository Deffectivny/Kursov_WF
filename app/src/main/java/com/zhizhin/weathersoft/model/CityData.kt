package com.zhizhin.weathersoft.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Ответ Open-Meteo Geocoding API со списком найденных городов.
 */
@Serializable
data class CitySearchResponse(
    @SerialName("results") val results: List<CityData> = emptyList()
)

/**
 * Модель одного города из результата поиска.
 */
@Serializable
data class CityData(
    @SerialName("name") val name: String,
    @SerialName("latitude") val latitude: Double,
    @SerialName("longitude") val longitude: Double,
    @SerialName("country") val country: String = ""
)
