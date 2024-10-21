package com.example.weatherapp.viewModel

import androidx.lifecycle.ViewModel
import com.example.weatherapp.data.WeatherUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class WeatherViewModel: ViewModel() {
    private val _uiState = MutableStateFlow(WeatherUiState("", ""))
    val uiState: StateFlow<WeatherUiState> =_uiState.asStateFlow()

    fun setCityName(cityNameFromSearchBar: String) {
        _uiState.update { currentState ->
            currentState.copy(cityName = cityNameFromSearchBar)
        }
    }
}