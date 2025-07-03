package com.team12.climaapp.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.team12.climaapp.utils.Constants
import com.team12.climaapp.model.WeatherResponse
import com.team12.climaapp.model.ForecastResponse
import com.team12.climaapp.network.RetrofitInstance
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun MainScreen() {
    var city by remember { mutableStateOf("Monterrey") }
    var weather by remember { mutableStateOf<WeatherResponse?>(null) }
    var forecast by remember { mutableStateOf<ForecastResponse?>(null) }
    var error by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF1D1E33), Color(0xFF111328))
                )
            )
            .padding(20.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.align(Alignment.TopCenter)
        ) {
            Spacer(modifier = Modifier.height(30.dp))

            TextField(
                value = city,
                onValueChange = { city = it },
                label = { Text("Ciudad") },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    disabledContainerColor = Color.LightGray,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedLabelColor = Color.Gray,
                    unfocusedLabelColor = Color.Gray,
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black
                )
            )

            Button(onClick = {
                scope.launch {
                    try {
                        weather = RetrofitInstance.api.getWeatherByCity(city, Constants.API_KEY)
                        val lat = weather?.cord?.lat ?: 0.0
                        val lon = weather?.cord?.lon ?: 0.0
                        forecast = RetrofitInstance.api.getFiveDayForecast(lat, lon, Constants.API_KEY)
                        error = ""
                    } catch (e: Exception) {
                        weather = null
                        forecast = null
                        error = "No se pudo cargar el clima o pronóstico."
                    }
                }
            }) {
                Text("Buscar clima y pronóstico")
            }

            weather?.let {
                Spacer(modifier = Modifier.height(30.dp))
                Text(text = it.name, fontSize = 24.sp, color = Color.White)

                AsyncImage(
                    model = "https://openweathermap.org/img/wn/${it.weather[0].icon}@4x.png",
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.size(140.dp)
                )

                Text(
                    text = "${it.main.temp.toInt()}°C",
                    fontSize = 48.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = it.weather[0].description.replaceFirstChar { c -> c.uppercase() },
                    fontSize = 16.sp,
                    color = Color.LightGray
                )

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    InfoItem(label = "Humedad", value = "${it.main.humidity}%")
                    // Puedes agregar más InfoItem aquí
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            forecast?.let { fcast ->
                val groupedByDate = fcast.list.groupBy { it.dt_txt.substring(0, 10) }

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    groupedByDate.forEach { (date, itemsForDate) ->
                        item {
                            Text(
                                text = getFormattedDate(date),
                                color = Color.White,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(start = 8.dp)
                            )
                        }
                        item {
                            LazyRow(
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                items(itemsForDate) { item ->
                                    ForecastItem(
                                        hour = item.dt_txt.substring(11, 16), // HH:mm
                                        temp = item.main.temp.toInt(),
                                        icon = item.weather[0].icon
                                    )
                                }
                            }
                        }
                    }
                }
            }

            if (error.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = error, color = Color.Red)
            }
        }
    }
}

@Composable
fun InfoItem(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = value, color = Color.White, fontWeight = FontWeight.Bold)
        Text(text = label, color = Color.LightGray, fontSize = 12.sp)
    }
}

@Composable
fun ForecastItem(hour: String, temp: Int, icon: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .background(Color(0xFF2C2F48), shape = MaterialTheme.shapes.medium)
            .padding(12.dp)
    ) {
        AsyncImage(
            model = "https://openweathermap.org/img/wn/${icon}@2x.png",
            contentDescription = null,
            modifier = Modifier.size(48.dp)
        )
        Text(text = hour, color = Color.White, fontSize = 14.sp)
        Text(text = "$temp°C", color = Color.White, fontWeight = FontWeight.Bold)
    }
}

fun getFormattedDate(dateStr: String): String {
    val parser = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val formatter = SimpleDateFormat("EEE dd MMM", Locale.getDefault())
    val date = parser.parse(dateStr)
    return if (date != null) formatter.format(date) else dateStr
}
