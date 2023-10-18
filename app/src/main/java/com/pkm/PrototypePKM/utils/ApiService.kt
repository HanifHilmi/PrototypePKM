package com.pkm.PrototypePKM.utils

import com.google.gson.JsonObject
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST


interface ApiService {
    @GET("getlatestdata.php")
    suspend fun getLatestData(): ResponseData

    @GET("getdata.php")
    suspend fun getData():ResponseData

    @GET("model.php")
    suspend fun getHasilPrediksiPanen():JsonObject

    @POST("inagro.php")
    suspend fun postInAgro(@Body requestBody: RequestBody): Response<Unit>

}


interface ForecastAPIService {
    @GET("weather/jawa-barat/soreang")
    suspend fun getForecastData():JsonObject
}
