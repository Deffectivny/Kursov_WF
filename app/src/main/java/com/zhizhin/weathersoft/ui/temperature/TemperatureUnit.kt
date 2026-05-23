package com.zhizhin.weathersoft.ui.temperature

import kotlin.math.roundToInt

enum class TemperatureUnit(val symbol: String) {
    Celsius("°C"),
    Fahrenheit("°F");

    fun convertFromCelsius(valueC: Double): Double {
        return when (this) {
            Celsius -> valueC
            Fahrenheit -> (valueC * 9.0 / 5.0) + 32.0
        }
    }

    fun formatFromCelsius(valueC: Double): String {
        val v = convertFromCelsius(valueC).roundToInt()
        return "$v$symbol"
    }
}

