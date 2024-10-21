package com.example.weatherapp

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.weatherapp.config.RetrofitClient
import com.example.weatherapp.service.WeatherServiceImpl
import com.google.gson.JsonObject
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

@Composable
fun ResultScreen(modifier: Modifier = Modifier, cityName: String) {
    val weatherService = WeatherServiceImpl(RetrofitClient.weatherRetrofitClient)
    var weatherData1 by remember { mutableStateOf<JsonObject?>(null) }

    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        coroutineScope.launch {
            try {
                val job1 = async { weatherService.getCityWeather(cityName) }
                weatherData1 = job1.await() // Await until the response is received
            } catch (e: Exception) {
                Log.e("Main", "Error occurred while fetching weather: ${e.message}")
                throw e
            }
        }
    }

    Column(modifier = modifier) {
        weatherData1?.let { data ->
            val main1: JsonObject = data.getAsJsonObject("main")
            val temp1: String = main1.get("temp").asString
            Text(text = temp1)
        } ?: Text(text = "Loading $cityName Weather...")

        Spacer(modifier = Modifier.height(10.dp))
    }
}