package com.pkm.PrototypePKM.utils

import com.pkm.PrototypePKM.R




data class WeatherData(
    val alat1_id: String,
    val alat1_tanggal: String,
    val alat1_waktu: String,
    val alat1_suhu: String,
    val alat1_kelembaban: String,
    val alat1_kec_angin: String,
    val alat1_radiasi: String,
    val alat2_id: String,
    val alat2_tanggal: String,
    val alat2_waktu: String,
    val alat2_curah_hujan: String
)

class CurrentWeatherData(
    suhu: Double,
    kelembapan: Int,
    curah_hujan : Float,
    cuaca:String
):BaseWeatherData(cuaca){
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
):BaseWeatherData(cuaca){
    var tanggal:String
    init {
        this.tanggal = tanggal
    }

}


sealed class BaseWeatherData(val cuaca: String) {
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

