package com.zhizhin.weathersoft.data

import com.zhizhin.weathersoft.model.CityData

/**
 * CityRepository — слой, который упрощает работу с сервисом поиска городов.
 */
class CityRepository(private val apiService: CityApiService) {

    /**
     * Ищем координаты города по названию и берём первый подходящий результат.
     */
    suspend fun getCityCoordinates(cityName: String): CityData? {
        return try {
            apiService.getCityInfo(cityName).results.firstOrNull()
        } catch (e: Exception) {
            null
        }
    }

    /**
     * Возвращаем список городов для автодополнения.
     */
    suspend fun searchCities(query: String): List<CityData> {
        return try {
            apiService.getCityInfo(query).results
        } catch (e: Exception) {
            emptyList()
        }
    }
}
