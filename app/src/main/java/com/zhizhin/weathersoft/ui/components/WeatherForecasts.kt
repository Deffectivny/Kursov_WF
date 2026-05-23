package com.zhizhin.weathersoft.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.zhizhin.weathersoft.model.WeatherForecast
import com.zhizhin.weathersoft.ui.temperature.TemperatureUnit

/**
 * Блок списка прогнозов.
 * Группирует все прогнозы по городу и показывает карточки по датам.
 */
@Composable
fun WeatherForecasts(
    forecasts: List<WeatherForecast>,
    tempUnit: TemperatureUnit
) {
    if (forecasts.isEmpty()) return

    Column(modifier = Modifier.padding(top = 8.dp)) {
        Text(
            text = "Прогноз погоды",
            style = MaterialTheme.typography.titleLarge
        )

        val groupedForecasts = forecasts.groupBy { it.city }

        LazyColumn {
            groupedForecasts.forEach { (city, cityForecasts) ->
                item {
                    Column(modifier = Modifier.padding(vertical = 8.dp)) {
                        Text(
                            text = city,
                            style = MaterialTheme.typography.titleMedium
                        )
                        Column {
                            cityForecasts.forEach { forecast ->
                                ForecastItem(
                                    forecast = forecast,
                                    tempUnit = tempUnit
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
