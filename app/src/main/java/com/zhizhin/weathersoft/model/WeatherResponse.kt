package com.zhizhin.weathersoft.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.InternalSerializationApi
import kotlin.OptIn

/**
 * Полный ответ Open-Meteo для прогноза.
 * Содержит общую информацию и блоки dailyUnits + daily (массивы значений).
 */
@OptIn(InternalSerializationApi::class)
@Serializable
data class WeatherResponse(
    @SerialName("latitude") val latitude: Double,
    @SerialName("longitude") val longitude: Double,
    @SerialName("generationtime_ms") val generationTimeMs: Double,
    @SerialName("utc_offset_seconds") val utcOffsetSeconds: Int,
    @SerialName("timezone") val timezone: String,
    @SerialName("timezone_abbreviation") val timezoneAbbreviation: String,
    @SerialName("elevation") val elevation: Double,
    @SerialName("daily_units") val dailyUnits: DailyUnits,
    @SerialName("daily") val daily: DailyData
)
