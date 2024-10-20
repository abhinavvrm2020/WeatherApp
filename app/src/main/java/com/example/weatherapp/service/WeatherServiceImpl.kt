package com.example.weatherapp.service

import android.util.Log
import com.example.weatherapp.client.WeatherRetrofitClient
import com.google.gson.JsonObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class WeatherServiceImpl(
    private val weatherRetrofitClient: WeatherRetrofitClient
) : WeatherService {
    override suspend fun getCityWeather(cityName: String): JsonObject {
        return try {
            // here using io dispatcher since performing network call
            withContext(Dispatchers.IO) {
                val response = weatherRetrofitClient.getCityWeather(
                    cityName = cityName,
                    apiKey = "f983b8da1cmsh5821288506d498dp1c7e7bjsn5a80c4dd5abb",
                    apiHost = "open-weather13.p.rapidapi.com"
                )
                Log.d("Main", "Weather from API: $response")
                response
            }
        } catch (e: Exception) {
            Log.e("Main", "Failed to get weather: ${e.message}")
            JsonObject()
        }
    }
}