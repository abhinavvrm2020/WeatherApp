package com.example.weatherapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.weatherapp.config.RetrofitClient
import com.example.weatherapp.service.WeatherServiceImpl
import com.example.weatherapp.ui.theme.WeatherAppTheme
import com.google.gson.JsonObject
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WeatherAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    HomeScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun HomeScreen(modifier: Modifier = Modifier) {
    var cityName by remember { mutableStateOf("") }
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TextField(
                    value = cityName,
                    onValueChange = { cityName = it },
                    label = { Text("City Name") },
                    placeholder = { Text("Type something...") },
                    singleLine = true,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
    WeatherScreen(modifier = Modifier.fillMaxSize(), cityName)
}

@Composable
fun WeatherScreen(modifier: Modifier = Modifier, cityName: String) {
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