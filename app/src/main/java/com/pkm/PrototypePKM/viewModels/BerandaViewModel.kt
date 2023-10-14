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

    private val _weatherData = MutableStateFlow<WeatherData?>(null)
    val weatherData: StateFlow<WeatherData?> = _weatherData

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
                    _weatherData.value = response[0]
                } catch (e: Exception) {
                    // Handle any errors or retry logic here
                }
                delay(DATA_REQUEST_INTERVAL)
            }
        }
    }

}



interface ApiService {
    @GET("getlatestdata.php")
    suspend fun getLatestData(): List<WeatherData>
}

