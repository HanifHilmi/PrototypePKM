package com.pkm.PrototypePKM.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pkm.PrototypePKM.utils.API_TEST
import com.pkm.PrototypePKM.utils.Alat1
import com.pkm.PrototypePKM.utils.Alat2
import com.pkm.PrototypePKM.utils.ApiService
import com.pkm.PrototypePKM.utils.DATA_REQUEST_INTERVAL
import com.pkm.PrototypePKM.utils.ForecastAPIService
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class BerandaViewModel : ViewModel() {

    private val _latestalat1Data = MutableStateFlow<Alat1?>(null)
    private val _latestalat2Data = MutableStateFlow<Alat2?>(null)
    val latestAlat1Data: StateFlow<Alat1?> = _latestalat1Data
    val latestAlat2Data: StateFlow<Alat2?> = _latestalat2Data

    private val _multiAlat1Data = MutableStateFlow<List<Alat1>>(emptyList())
    private val _multiAlat2Data = MutableStateFlow<List<Alat2>>(emptyList())
    val multiAlat1 = _multiAlat1Data
    val multiAlat2 = _multiAlat2Data


    private val _dataCuacaList = MutableStateFlow<List<Triple<String,String,String>>>(emptyList())
    val dataCuacaList = _dataCuacaList


    private val apiService: ApiService
    private val forecastAPI: ForecastAPIService

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://pkm.fewsstmkg.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()


        val retrofitForcast = Retrofit.Builder()
            .baseUrl("https://cuaca-gempa-rest-api.vercel.app")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        apiService = retrofit.create(ApiService::class.java)
        forecastAPI = retrofitForcast.create(ForecastAPIService::class.java)
        fetchAndUpdateData()
        fetchForecastData()
    }

    private fun fetchAndUpdateData() {
        viewModelScope.launch {
            var count = 0
            while (true) {
                Log.d(API_TEST,"request count ${++count}")
                try {
                    val response = apiService.getLatestData()
                    Log.d(API_TEST,"response ${response}")
                    _latestalat1Data.value = response.alat1[0]
                    _latestalat2Data.value = response.alat2[0]
                } catch (e: Exception) {
                    // Handle any errors or retry logic here
                }
                delay(DATA_REQUEST_INTERVAL)
            }
        }
    }

    fun fetchMultiData(){
        viewModelScope.launch {
            try {
                val response = apiService.getData()
                _multiAlat1Data.value = response.alat1
                _multiAlat2Data.value = response.alat2
            }catch (e:Exception){
                // Handle any errors or retry logic here
            }
        }


    }


    fun fetchForecastData(){
        viewModelScope.launch {
            try{
                val response= forecastAPI.getForecastData()
                val data = response.getAsJsonObject("data").getAsJsonArray("params")

//                val humidityHourly = data.get(0)
//                    .asJsonObject
//                    .getAsJsonArray("times")
//                    .map {
//                        val h = it.asJsonObject.getAsJsonPrimitive("h").toString()
//                        val datetime = it.asJsonObject.getAsJsonPrimitive("datetime").toString()
//                        val hum = it.asJsonObject.getAsJsonPrimitive("value").toString()
//                        Triple(h,datetime,hum)
//                    }

                val weatherHourly = data.get(6)
                    .asJsonObject
                    .getAsJsonArray("times")
                    .map {
                        val code = it.asJsonObject.getAsJsonPrimitive("code").toString().replace("\"","")
                        val datetime = it.asJsonObject.getAsJsonPrimitive("datetime").toString().replace("\"","")
                        val jam = it.asJsonObject.getAsJsonPrimitive("h").toString().replace("\"","")
                        Triple(code,datetime,jam)
                    }

                _dataCuacaList.value = weatherHourly
                Log.d("Forecast test",data.toString())
                Log.d("Forecast test","Weather $weatherHourly")
            }catch (e:Exception){
                Log.e("Forecast test error",e.toString())
                // Handle any errors or retry logic here
            }
        }
    }

}



