package com.zhizhin.weathersoft.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.stringResource
import com.zhizhin.weathersoft.R
import com.zhizhin.weathersoft.ui.components.CitiesList
import com.zhizhin.weathersoft.ui.components.ErrorMessage
import com.zhizhin.weathersoft.ui.components.InputSection
import com.zhizhin.weathersoft.ui.components.WeatherForecasts
import com.zhizhin.weathersoft.ui.temperature.TemperatureUnit
import com.zhizhin.weathersoft.viewmodel.WeatherViewModel

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainScreen(
    viewModel: WeatherViewModel
) {
    var cityName by remember { mutableStateOf("") }
    var days by remember { mutableIntStateOf(7) }
    var tempUnit by rememberSaveable { mutableStateOf(TemperatureUnit.Celsius) }

    val state = viewModel.uiState

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.25f),
                        MaterialTheme.colorScheme.background
                    )
                )
            )
    ) {
        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = stringResource(R.string.app_name),
                            style = MaterialTheme.typography.titleLarge
                        )
                    }
                )
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                state.errorMessage?.let { message ->
                    ErrorMessage(
                        message = message,
                        onDismiss = { viewModel.clearErrorMessage() }
                    )
                }

                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.large,
                    tonalElevation = 4.dp,
                    shadowElevation = 4.dp
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        InputSection(
                            cityName = cityName,
                            days = days,
                            citySuggestions = state.citySuggestions,
                            tempUnit = tempUnit,
                            onCityQueryChange = { text ->
                                cityName = text
                                viewModel.fetchCitySuggestions(text)
                            },
                            onSuggestionSelected = { suggestion ->
                                // Подсказки приходят в формате "London, GB" - оставляем только название.
                                cityName = suggestion.substringBefore(",").trim()
                                viewModel.clearSuggestions()
                            },
                            onForecastDaysChange = { days = it },
                            onTrackCity = {
                                viewModel.trackCity(it)
                                cityName = ""
                                viewModel.clearSuggestions()
                            },
                            onToggleTempUnit = {
                                tempUnit = when (tempUnit) {
                                    TemperatureUnit.Celsius -> TemperatureUnit.Fahrenheit
                                    TemperatureUnit.Fahrenheit -> TemperatureUnit.Celsius
                                }
                            },
                            onRefreshForecasts = {
                                // Если город набран, но не добавлен - добавим автоматически.
                                if (cityName.isNotBlank()) {
                                    viewModel.trackCity(cityName)
                                    cityName = ""
                                }
                                viewModel.clearSuggestions()
                                viewModel.refreshForecastsForTrackedCities(days)
                            }
                        )

                        CitiesList(
                            cities = state.trackedCities,
                            onRemoveCity = { cityToRemove ->
                                viewModel.untrackCity(cityToRemove)
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                if (state.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.CenterHorizontally)
                    )
                } else {
                    WeatherForecasts(
                        forecasts = state.forecasts,
                        tempUnit = tempUnit
                    )
                }
            }
        }
    }
}
