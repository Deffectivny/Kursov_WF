package com.zhizhin.weathersoft.data

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

/**
 * ApiClient — это объект, который создаётся один раз на всё приложение.
 *
 * Зачем он нужен:
 * 1) Настроить работу с сетью (OkHttp)
 * 2) Настроить Retrofit для каждого сервиса (города / погода)
 * 3) Создать репозитории, чтобы ViewModel не знала детали API
 *
 * Итог: вся “сетевая кухня” лежит здесь, а в ViewModel мы просто вызываем методы репозиториев.
 */
object ApiClient {

    /**
     * json — настройки для kotlinx.serialization.
     * Это конвертер, который превращает JSON из интернета в наши data class'ы.
     */
    private val json = Json {
        // Если API пришлёт лишние поля, которых нет в модели — не падаем
        ignoreUnknownKeys = true

        // Разрешаем “неидеальный” JSON (иногда APIs шлют нестрогий формат)
        isLenient = true
    }

    /**
     * logging — перехватчик, который печатает все запросы и ответы в Logcat.
     * Очень удобно для отладки: видно URL, параметры и что вернул сервер.
     */
    private val logging = HttpLoggingInterceptor().apply {
        // BODY = печатает и заголовки, и тело ответа (максимально подробно)
        level = HttpLoggingInterceptor.Level.BODY
    }

    /**
     * okHttp — клиент, который реально делает запросы по сети.
     * Retrofit внутри использует OkHttp, поэтому мы настраиваем его один раз.
     */
    private val okHttp = OkHttpClient.Builder()
        // подключаем логирование запросов/ответов
        .addInterceptor(logging)
        .build()

    /**
     * retrofitCity — Retrofit для Open-Meteo Geocoding API (поиск координат города).
     *
     * baseUrl ОБЯЗАТЕЛЬНО должен заканчиваться слэшем.
     * Конвертер — превращает JSON в объекты Kotlin.
     */
    private val retrofitCity = Retrofit.Builder()
        .baseUrl("https://geocoding-api.open-meteo.com/v1/")
        .client(okHttp)
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .build()

    /**
     * retrofitWeather — Retrofit для Open-Meteo (прогноз погоды по координатам).
     */
    private val retrofitWeather = Retrofit.Builder()
        .baseUrl("https://api.open-meteo.com/v1/")
        .client(okHttp)
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .build()

    /**
     * Создаём реализации интерфейсов API.
     * Retrofit сам генерирует код, который будет делать HTTP запросы.
     */
    private val cityApi: CityApiService =
        retrofitCity.create(CityApiService::class.java)

    private val weatherApi: WeatherApiService =
        retrofitWeather.create(WeatherApiService::class.java)

    /**
     * Репозитории — это слой абстракции над API.
     * ViewModel обращается к репозиториям, а не напрямую к Retrofit.
     *
     * by lazy означает: объект создастся только при первом обращении.
     */
    private val cityRepo by lazy { CityRepository(cityApi) }
    private val weatherRepo by lazy { WeatherRepository(weatherApi) }

    /**
     * Эти функции вызываются в MainActivity, чтобы передать репозитории в ViewModelFactory.
     */
    fun provideCityRepository(): CityRepository = cityRepo
    fun provideWeatherRepository(): WeatherRepository = weatherRepo
}
