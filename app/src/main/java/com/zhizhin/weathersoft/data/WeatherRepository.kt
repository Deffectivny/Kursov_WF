package com.zhizhin.weathersoft.data

import com.zhizhin.weathersoft.model.WeatherResponse

/**
 * WeatherRepository — обёртка над WeatherApiService.
 * Делает вызов API и ловит ошибки, чтобы приложение не падало.
 */
class WeatherRepository(
    private val apiService: WeatherApiService
) {
    /**
     * Запрашивает прогноз погоды.
     * Возвращает WeatherResponse или null (если ошибка).
     */
    suspend fun getWeatherForecast(
        latitude: Double,
        longitude: Double,
        days: Int
    ): WeatherResponse? {
        return try {
            apiService.getWeatherForecast(
                latitude = latitude,
                longitude = longitude,
                days = days
            )
        } catch (e: Exception) {
            null
        }
    }
}
