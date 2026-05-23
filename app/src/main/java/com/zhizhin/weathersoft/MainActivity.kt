package com.zhizhin.weathersoft

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.zhizhin.weathersoft.data.ApiClient
import com.zhizhin.weathersoft.ui.screens.MainScreen
import com.zhizhin.weathersoft.ui.theme.WeatherSoftTheme
import com.zhizhin.weathersoft.viewmodel.WeatherViewModel
import com.zhizhin.weathersoft.viewmodel.WeatherViewModelFactory

/**
 * Главная (и единственная) Activity приложения.
 * Здесь подключаем ViewModel и запускаем Compose UI.
 */
class MainActivity : ComponentActivity() {

    // Инициализируем ViewModel через делегат by viewModels и нашу фабрику
    private val weatherViewModel: WeatherViewModel by viewModels {
        WeatherViewModelFactory(
            cityRepository = ApiClient.provideCityRepository(),
            weatherRepository = ApiClient.provideWeatherRepository()
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // setContent – точка входа в Compose UI
        setContent {
            WeatherSoftTheme {
                Surface(
                    modifier = Modifier,
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Передаём ViewModel в главный экран
                    MainScreen(viewModel = weatherViewModel)
                }
            }
        }
    }
}
