package com.pkm.PrototypePKM.utils

import com.pkm.PrototypePKM.R

class CurrentWeatherData(
    suhu: Double,
    kelembapan: Int,
    curah_hujan : Float,
    cuaca:String
):WeatherData(cuaca){
    var suhu: Double
    var kelembapan:Int
    var curah_hujan:Float

    init {
        this.suhu = suhu
        this.kelembapan = kelembapan
        this.curah_hujan = curah_hujan
    }

}

class ForecastWeatherData(
    tanggal:String,
    cuaca:String
):WeatherData(cuaca){
    var tanggal:String
    init {
        this.tanggal = tanggal
    }

}


sealed class WeatherData(val cuaca: String) {
    fun getIconId(): Int{
        return when(cuaca){
            "Cerah" -> R.drawable.icon_cuaca_cerah
            "Cerah Berawan" -> R.drawable.icon_cuaca_cerahberawan
            "Berawan" -> R.drawable.icon_cuaca_berawan
            "Hujan Ringan" -> R.drawable.icon_cuaca_hujan_ringan
            "Hujan" -> R.drawable.icon_cuaca_hujan
            "Hujan Petir" -> R.drawable.icon_cuaca_hujan_petir
            else -> 0
        }
    }
}

