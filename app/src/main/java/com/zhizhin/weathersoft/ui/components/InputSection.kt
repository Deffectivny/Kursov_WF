package com.zhizhin.weathersoft.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.zhizhin.weathersoft.ui.temperature.TemperatureUnit

/**
 * Верхний блок управления:
 *  - поле ввода города
 *  - подсказки по городам
 *  - слайдер количества дней
 *  - кнопки "Добавить город" и "Обновить прогноз"
 */
@Composable
fun InputSection(
    cityName: String,
    days: Int,
    citySuggestions: List<String>,
    tempUnit: TemperatureUnit,
    onCityQueryChange: (String) -> Unit,
    onSuggestionSelected: (String) -> Unit,
    onForecastDaysChange: (Int) -> Unit,
    onTrackCity: (String) -> Unit,
    onToggleTempUnit: () -> Unit,
    onRefreshForecasts: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        OutlinedTextField(
            value = cityName,
            onValueChange = onCityQueryChange,
            label = { Text("Город") },
            placeholder = { Text("Например, London или Москва") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                capitalization = KeyboardCapitalization.Words,
                autoCorrect = true
            )
        )

        // Подсказки городов под полем ввода
        if (citySuggestions.isNotEmpty()) {
            Column(modifier = Modifier.fillMaxWidth()) {
                citySuggestions.forEach { suggestion ->
                    Text(
                        text = suggestion,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onSuggestionSelected(suggestion) }
                            .padding(vertical = 4.dp)
                    )
                }
            }
        }

        // Слайдер для выбора количества дней прогноза
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text("Дней прогноза:")
            Slider(
                value = days.toFloat(),
                onValueChange = { onForecastDaysChange(it.toInt()) },
                valueRange = 1f..16f,
                steps = 15,
                modifier = Modifier.weight(1f)
            )
            Text("$days")
        }

        // Кнопки управления
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = { onTrackCity(cityName) },
                enabled = cityName.isNotBlank(),
                modifier = Modifier.weight(1f)
            ) {
                Text("Добавить город")
            }

            FilledTonalIconButton(
                onClick = onToggleTempUnit,
                modifier = Modifier.size(36.dp)
            ) {
                Text(tempUnit.symbol)
            }

            Button(
                onClick = onRefreshForecasts,
                modifier = Modifier.weight(1f)
            ) {
                Text("Обновить прогноз")
            }
        }
    }
}
