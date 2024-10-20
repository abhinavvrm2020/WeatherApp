package com.example.weatherapp.config

import com.example.weatherapp.client.WeatherRetrofitClient
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "https://open-weather13.p.rapidapi.com/city/"
    private val okHttpClient = OkHttpClient.Builder().build()

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val weatherRetrofitClient: WeatherRetrofitClient =
        retrofit.create(WeatherRetrofitClient::class.java)
}
