package com.team12.climaapp.model

data class ForecastResponse(
    val cod: String,
    val message: Int,
    val cnt: Int,
    val list: List<ForecastItem>,
    val city: City
)

// El resto de tus clases ya están bien definidas:
data class ForecastItem(
    val dt: Long,
    val main: Main,                    // Reutilizada
    val weather: List<Weather>,       // Reutilizada
    val clouds: Clouds,
    val wind: Wind,
    val visibility: Int,
    val pop: Double,
    val rain: Rain?,
    val snow: Snow?,
    val sys: Sys,
    val dt_txt: String
)


data class Main(
    val temp: Double,
    val feels_like: Double,
    val temp_min: Double,
    val temp_max: Double,
    val pressure: Int,
    val sea_level: Int?,
    val grnd_level: Int?,
    val humidity: Int,
    val temp_kf: Double?
)

data class Weather(
    val id: Int,
    val main: String,
    val description: String,
    val icon: String
)

data class WeatherResponse(
    val name: String,
    val cord: Coord,
    val main: Main,
    val weather: List<Weather>
)


data class Clouds(val all: Int)

data class Wind(
    val speed: Double,
    val deg: Int,
    val gust: Double?
)

data class Rain(val `3h`: Double?)

data class Snow(val `3h`: Double?)

data class Sys(val pod: String)

data class City(
    val id: Int,
    val name: String,
    val cord: Coord,
    val country: String,
    val population: Int?,
    val timezone: Int?,
    val sunrise: Long?,
    val sunset: Long?
)

data class Coord(
    val lat: Double,
    val lon: Double
)
