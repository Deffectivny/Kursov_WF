package com.zhizhin.weathersoft.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.zhizhin.weathersoft.data.CityRepository
import com.zhizhin.weathersoft.data.WeatherRepository

/**
 * Фабрика ViewModel, чтобы передать в WeatherViewModel наши репозитории.
 * Используется в MainActivity при создании ViewModel через делегат by viewModels().
 */
class WeatherViewModelFactory(
    private val cityRepository: CityRepository,
    private val weatherRepository: WeatherRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WeatherViewModel::class.java)) {
            return WeatherViewModel(
                cityRepository = cityRepository,
                weatherRepository = weatherRepository
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}
