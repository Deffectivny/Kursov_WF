package com.zhizhin.weathersoft.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zhizhin.weathersoft.data.CityRepository
import com.zhizhin.weathersoft.data.WeatherRepository
import com.zhizhin.weathersoft.model.WeatherForecast
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

/**
 * Состояние экрана прогноза погоды.
 * Хранится целиком в одном data class и обновляется через copy().
 */
data class WeatherScreenState(
    val trackedCities: List<String> = emptyList(),          // Города, добавленные пользователем
    val forecasts: List<WeatherForecast> = emptyList(),     // Все прогнозы по городам
    val isLoading: Boolean = false,                         // Индикатор загрузки
    val errorMessage: String? = null,                       // Сообщение об ошибке
    val citySuggestions: List<String> = emptyList()         // Подсказки городов для автодополнения
)

/**
 * WeatherViewModel - связующее звено между UI и репозиториями.
 * Здесь:
 *  - храним состояние экрана
 *  - запрашиваем данные из сети
 *  - обрабатываем ошибки
 */
class WeatherViewModel(
    private val cityRepository: CityRepository,
    private val weatherRepository: WeatherRepository
) : ViewModel() {

    // Состояние экрана, к которому подписан интерфейс (Compose)
    var uiState by mutableStateOf(WeatherScreenState())
        private set

    // Job для последнего поиска подсказок (чтобы отменять старые запросы)
    private var searchJob: Job? = null

    /**
     * Добавление нового города в список отслеживаемых.
     */
    fun trackCity(city: String) {
        val normalized = city.trim()
        if (normalized.isEmpty()) return

        if (normalized !in uiState.trackedCities) {
            uiState = uiState.copy(
                trackedCities = uiState.trackedCities + normalized,
                errorMessage = null
            )
        }
    }

    /**
     * Удаление города из списка + очистка его прогнозов.
     */
    fun untrackCity(city: String) {
        uiState = uiState.copy(
            trackedCities = uiState.trackedCities - city,
            forecasts = uiState.forecasts.filterNot { it.city == city }
        )
    }

    /**
     * Загрузка прогнозов для всех выбранных городов.
     */
    fun refreshForecastsForTrackedCities(days: Int) {
        viewModelScope.launch {
            if (uiState.trackedCities.isEmpty()) {
                uiState = uiState.copy(
                    errorMessage = "Добавьте хотя бы один город"
                )
                return@launch
            }

            uiState = uiState.copy(
                isLoading = true,
                errorMessage = null
            )

            val allForecasts = mutableListOf<WeatherForecast>()
            val errors = mutableListOf<String>()

            for (city in uiState.trackedCities) {
                try {
                    val forecastsForCity = loadForecastForCity(city, days)
                    allForecasts += forecastsForCity
                } catch (e: Exception) {
                    errors += "$city: ${e.message ?: "не удалось загрузить"}"
                }
            }

            uiState = uiState.copy(
                isLoading = false,
                forecasts = allForecasts,
                errorMessage = if (errors.isNotEmpty()) {
                    "Не удалось обновить прогноз для: ${errors.joinToString()}"
                } else null
            )
        }
    }

    /**
     * Внутренняя функция: загрузка прогноза только для одного города.
     */
    private suspend fun loadForecastForCity(city: String, days: Int): List<WeatherForecast> {
        // 1. Получаем координаты города
        val cityInfo = cityRepository.getCityCoordinates(city)
            ?: throw IllegalArgumentException("Город \"$city\" не найден")

        // 2. Запрашиваем прогноз по координатам
        val weatherResponse = weatherRepository.getWeatherForecast(
            latitude = cityInfo.latitude,
            longitude = cityInfo.longitude,
            days = days
        ) ?: throw IllegalStateException("Сервер погоды вернул пустой ответ")

        // 3. Преобразуем ответ Open-Meteo в список моделей для UI
        return weatherResponse.daily.time.mapIndexed { index, date ->
            WeatherForecast(
                city = city,
                date = date,
                maxTemp = weatherResponse.daily.maxTemperatures[index],
                minTemp = weatherResponse.daily.minTemperatures[index]
            )
        }
    }

    /**
     * Очистить сообщение об ошибке (после закрытия диалога).
     */
    fun clearErrorMessage() {
        uiState = uiState.copy(errorMessage = null)
    }

    // ---------- АВТОДОПОЛНЕНИЕ ГОРОДОВ ----------

    /**
     * Поиск подсказок по названию города.
     * Если введено меньше 3 символов - подсказки скрываются.
     */
    fun fetchCitySuggestions(query: String) {
        if (query.length < 3) {
            uiState = uiState.copy(citySuggestions = emptyList())
            searchJob?.cancel()
            return
        }

        // отменяем предыдущий запрос, чтобы не спамить API
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            val result = cityRepository.searchCities(query)
            val suggestions = result
                .distinctBy { it.name to it.country } // убираем дубликаты
                .take(5)                              // максимум 5 вариантов
                .map { "${it.name}, ${it.country}" }

            uiState = uiState.copy(citySuggestions = suggestions)
        }
    }

    /**
     * Полностью очистить список подсказок.
     */
    fun clearSuggestions() {
        uiState = uiState.copy(citySuggestions = emptyList())
    }
}
