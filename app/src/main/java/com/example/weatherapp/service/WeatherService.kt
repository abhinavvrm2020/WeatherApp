package com.example.weatherapp.service

import com.google.gson.JsonObject

interface WeatherService {
    suspend fun getCityWeather(cityName: String): JsonObject
}