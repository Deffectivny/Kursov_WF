package com.zhizhin.weathersoft.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.InternalSerializationApi
import kotlin.OptIn

/**
 * Единицы измерения для каждого поля DailyData.
 * Обычно используются только для информации (°C, iso8601 и т.п.).
 */
@OptIn(InternalSerializationApi::class)
@Serializable
data class DailyUnits(
    @SerialName("time") val timeUnit: String,
    @SerialName("temperature_2m_max") val maxTempUnit: String,
    @SerialName("temperature_2m_min") val minTempUnit: String
)
