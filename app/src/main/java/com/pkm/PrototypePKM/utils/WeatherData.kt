package com.pkm.PrototypePKM.utils




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

//data class ForecastResponse(
//    val success:Boolean,
//    val message:String?,
//    val data:ForecastData
//)

//data class ForecastData(
//    val id:String,
//    val latitude:String,
//    val longitude:String,
//    val coordinate:String,
//    val type:String,
//    val region:String,
//    val level:String,
//    val domain:String,
//    val tags:String,
//    val params:ForecastParams,
//)
//
//data class ForecastParams(
//    val id: String,
//    val description:String,
//    val type:String,
//    val times:ForecastTimes
//)
//
//data class ForecastTimes(
//    val type: String,
//    val day:String,
//    val dateTime:String,
//    val value:String
//)