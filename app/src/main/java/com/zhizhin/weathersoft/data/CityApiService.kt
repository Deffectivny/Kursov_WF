package com.zhizhin.weathersoft.data

import com.zhizhin.weathersoft.model.CitySearchResponse
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * CityApiService — описание запросов к Open-Meteo Geocoding API.
 * Сервис используется для поиска города и получения его координат.
 */
interface CityApiService {

    /**
     * GET https://geocoding-api.open-meteo.com/v1/search?name=London&count=10&language=ru&format=json
     */
    @GET("search")
    suspend fun getCityInfo(
        @Query("name") name: String,
        @Query("count") count: Int = 10,
        @Query("language") language: String = "ru",
        @Query("format") format: String = "json"
    ): CitySearchResponse
}
