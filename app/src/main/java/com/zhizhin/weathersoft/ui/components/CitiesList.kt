package com.zhizhin.weathersoft.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

/**
 * Блок со списком выбранных городов.
 * Можно свернуть, чтобы не вытеснять прогноз вниз.
 */
@Composable
fun CitiesList(
    cities: List<String>,
    onRemoveCity: (String) -> Unit
) {
    if (cities.isEmpty()) return

    var expanded by rememberSaveable { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .padding(top = 8.dp, bottom = 8.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Выбранные города:",
                style = MaterialTheme.typography.titleSmall
            )
        }

        if (expanded) {
            cities.forEach { city ->
                CityItem(city = city, onRemove = onRemoveCity)
            }
        } else {
            val maxCitiesInLine = 5
            val preview = cities.take(maxCitiesInLine)
            val suffix = if (cities.size > maxCitiesInLine) ", ..." else ""
            val oneLine = preview.joinToString(", ") + suffix
            Text(
                text = oneLine,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(start = 4.dp, end = 4.dp)
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 2.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { expanded = !expanded }) {
                Icon(
                    imageVector = if (expanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                    contentDescription = if (expanded) "Свернуть список" else "Развернуть список"
                )
            }
        }
    }
}
