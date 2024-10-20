package com.example.weatherapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.weatherapp.config.RetrofitClient
import com.example.weatherapp.service.WeatherServiceImpl
import com.example.weatherapp.ui.theme.WeatherAppTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WeatherAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    WeatherScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun WeatherScreen(modifier: Modifier = Modifier) {
    val weatherService = WeatherServiceImpl(RetrofitClient.weatherRetrofitClient)
    var weatherData1 by remember { mutableStateOf("Loading...") }
    var weatherData2 by remember { mutableStateOf("Loading...") }
    var weatherData3 by remember { mutableStateOf("Loading...") }

    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        coroutineScope.launch {
            try {
                // Replace "London" with the desired city name
                val job1 = launch {
                    weatherData1 = weatherService.getCityWeather("London").toString()
                }
                val job2 = launch {
                    weatherData2 = weatherService.getCityWeather("India").toString()
                }
                val job3 = launch {
                    weatherData3 = weatherService.getCityWeather("Paris").toString()
                }
            } catch (e: Exception) {
                weatherData1 = "Error: ${e.message}"
            }
        }
    }

    Column {
        Text(
            text = weatherData1,
            modifier = modifier
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = weatherData2,
            modifier = modifier
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = weatherData3,
            modifier = modifier
        )
    }
}

@Preview(showBackground = true)
@Composable
fun WeatherScreenPreview() {
    WeatherAppTheme {
        // Preview with static text as weather data
        Text(text = "Weather data will be displayed here")
    }
}
