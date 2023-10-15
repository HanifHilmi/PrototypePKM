package com.pkm.PrototypePKM.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pkm.PrototypePKM.utils.API_TEST
import com.pkm.PrototypePKM.utils.DATA_REQUEST_INTERVAL
import com.pkm.PrototypePKM.utils.WeatherData
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

class BerandaViewModel : ViewModel() {

    private val _latestWeatherData = MutableStateFlow<WeatherData?>(null)
    val latestWeatherData: StateFlow<WeatherData?> = _latestWeatherData

    private val _weatherData = MutableStateFlow<List<WeatherData>>(emptyList())
    val weatherData = _weatherData

    private val apiService: ApiService

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://pkm.fewsstmkg.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        apiService = retrofit.create(ApiService::class.java)
        fetchAndUpdateData()
    }

    private fun fetchAndUpdateData() {
        viewModelScope.launch {
            var count = 0
            while (true) {
                Log.d(API_TEST,"request count ${++count}")
                try {
                    val response = apiService.getLatestData()
                    _latestWeatherData.value = response[0]
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
                _weatherData.value = response
            }catch (e:Exception){
                // Handle any errors or retry logic here
            }
        }


    }

}



interface ApiService {
    @GET("getlatestdata.php")
    suspend fun getLatestData(): List<WeatherData>

    @GET("getdata.php")
    suspend fun getData():List<WeatherData>
}

