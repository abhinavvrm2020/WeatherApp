package com.example.weatherapp.client

import com.google.gson.JsonObject
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface WeatherRetrofitClient {
    @GET("{cityName}/EN")
    suspend fun getCityWeather(
        @Path("cityName") cityName : String,
        @Header("x-rapidapi-key") apiKey: String,
        @Header("x-rapidapi-host") apiHost: String
    ): JsonObject
}