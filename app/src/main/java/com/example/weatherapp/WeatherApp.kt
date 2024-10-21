package com.example.weatherapp

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.weatherapp.viewModel.WeatherViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

enum class WeatherAppScreen(@StringRes val title: Int) {
    Start(title = R.string.app_name),
    Search(title = R.string.search),
    Result(title = R.string.result),
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherAppBar(
    currentScreen: WeatherAppScreen,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = { Text(stringResource(currentScreen.title)) },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        modifier = modifier,
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back_button)
                    )
                }
            }
        }
    )
}

@Composable
fun WeatherApp(
    viewModel: WeatherViewModel = viewModel(),
    navController: NavHostController = rememberNavController()
) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = WeatherAppScreen.valueOf(
        backStackEntry?.destination?.route ?: WeatherAppScreen.Start.name
    )
    Scaffold (
        topBar = {
            WeatherAppBar(
                currentScreen = currentScreen,
                canNavigateBack = navController.previousBackStackEntry != null,
                navigateUp = {navController.navigateUp()}
            )
        }
    ) { innerPadding ->
        val uiState by viewModel.uiState.collectAsState()
        NavHost(
            navController = navController,
            startDestination = WeatherAppScreen.Start.name,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(route = WeatherAppScreen.Start.name) {
                HomeScreen(
                    R.string.app_name,
                    onNextButtonClicked = {
                        navController.navigate(WeatherAppScreen.Search.name)
                    }
                )
            }
            composable(route = WeatherAppScreen.Search.name) {
                SearchScreen(
                    onCityNameEntered = { viewModel.setCityName(it) },
                    onNextButtonClicked = { navController.navigate(WeatherAppScreen.Result.name) }
                )
            }
            composable(route = WeatherAppScreen.Result.name) {
                ResultScreen(
                    cityName = uiState.cityName
                )
            }
        }
    }
}

private fun cancelOrderAndNavigateToStart(
    viewModel: WeatherViewModel,
    navController: NavHostController
) {
    viewModel.resetDetails()
    navController.popBackStack(WeatherAppScreen.Start.name, inclusive = false)
}