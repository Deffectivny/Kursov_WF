package com.zhizhin.weathersoft.data

import com.zhizhin.weathersoft.model.WeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * WeatherApiService — описание запросов к Open-Meteo.
 *
 * Здесь мы получаем прогноз по координатам:
 * latitude/longitude -> daily прогноз.
 */
interface WeatherApiService {

    /**
     * GET https://api.open-meteo.com/v1/forecast?...params...
     *
     * daily = "temperature_2m_max,temperature_2m_min" -> просим только нужные поля
     * forecast_days -> количество дней прогноза (1..16)
     * timezone=auto -> сервер сам поставит правильный часовой пояс
     */
    @GET("forecast")
    suspend fun getWeatherForecast(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("daily") daily: String = "temperature_2m_max,temperature_2m_min",
        @Query("forecast_days") days: Int = 7,
        @Query("timezone") timezone: String = "auto"
    ): WeatherResponse
}
