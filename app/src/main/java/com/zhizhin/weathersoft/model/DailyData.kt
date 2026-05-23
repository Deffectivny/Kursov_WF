package com.zhizhin.weathersoft.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.InternalSerializationApi
import kotlin.OptIn

/**
 * DailyData – блок с массивами ежедневных значений в ответе Open-Meteo.
 * Все списки имеют одинаковую длину и индекс соответствия дню.
 */
@OptIn(InternalSerializationApi::class)
@Serializable
data class DailyData(
    // Список дат (строки формата "YYYY-MM-DD")
    @SerialName("time") val time: List<String>,
    // Максимальные температуры по дням
    @SerialName("temperature_2m_max") val maxTemperatures: List<Double>,
    // Минимальные температуры по дням
    @SerialName("temperature_2m_min") val minTemperatures: List<Double>
)
