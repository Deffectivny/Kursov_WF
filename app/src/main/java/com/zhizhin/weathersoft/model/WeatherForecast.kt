package com.zhizhin.weathersoft.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.InternalSerializationApi
import kotlin.OptIn

/**
 * Упрощённая модель прогноза для отображения в интерфейсе.
 * Не привязана к формату API, это уже "готовые" данные для UI.
 */
@OptIn(InternalSerializationApi::class)
@Serializable
data class WeatherForecast(
    val city: String,   // Название города
    val date: String,   // Дата прогноза
    val maxTemp: Double,
    val minTemp: Double
)
