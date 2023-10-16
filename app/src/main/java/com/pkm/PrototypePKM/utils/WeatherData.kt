package com.pkm.PrototypePKM.utils

import com.pkm.PrototypePKM.R




data class Alat1(
    val id: String,
    val tanggal: String,
    val waktu: String,
    val suhu: String,
    val kelembaban: String,
    val kec_angin: String,
    val radiasi: String,

)

data class Alat2(
    val id: String,
    val tanggal: String,
    val waktu: String,
    val curah_hujan: String

)

data class ResponseData(
    val alat1:List<Alat1>,
    val alat2:List<Alat2>
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

