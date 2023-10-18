package com.pkm.PrototypePKM.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.pkm.PrototypePKM.utils.API_TEST
import com.pkm.PrototypePKM.utils.ApiService
import com.pkm.PrototypePKM.utils.HasilPrediksi
import com.pkm.PrototypePKM.utils.InAgro
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class PrediksiPanenViewModel:ViewModel() {

    private val _hasilPrediksi = MutableStateFlow<HasilPrediksi?>(null)
    val hasilPrediksi = _hasilPrediksi

    private val apiService: ApiService

    var uiState = MutableStateFlow(PrediksiPanenUIstate())
        private set

    fun clearHasil(){ _hasilPrediksi.value = null }

    fun postData(requestData: InAgro,uuid:String) {

        val json = Json.encodeToString(requestData)
        Log.d(API_TEST,"json bdoy $json")
        val requestBody = json.toRequestBody("application/json".toMediaType())
        viewModelScope.launch {
            uiState.update { it.copy(loadingState = true) }
            try {
                Log.d(API_TEST,"API kirim InAgro")
                val response = apiService.postInAgro(requestBody)
                Log.d(API_TEST,"API kirim InAgro hasil ${response.code()} ${response}")
                fetchPrediksiPanenData(requestData, uuid)
            } catch (e: Exception) {
                // Error handling here
                Log.e(API_TEST,"API kirim InAgro $e")
                delay(1000)
                uiState.update { it.copy(loadingState = false, errorMessage = e.message) }
            }


        }
    }

    private fun fetchPrediksiPanenData(dataSent:InAgro, uuid: String){
        viewModelScope.launch {

            try {
                val response = apiService.getHasilPrediksiPanen()
                Log.d(API_TEST,"prediksi panen $response")

                val userGet = response.getAsJsonPrimitive("user").asString
                val countTotal = response.getAsJsonPrimitive("count").asInt
                val count1 = response.getAsJsonPrimitive("count1").asInt
                val count2 = response.getAsJsonPrimitive("count2").asInt
                val count3 = response.getAsJsonPrimitive("count3").asInt
                val count4 = response.getAsJsonPrimitive("count4").asInt
                val count5 = response.getAsJsonPrimitive("count5").asInt
                val count6 = response.getAsJsonPrimitive("count6").asInt


                Log.d(API_TEST,"API terima model user: $userGet total: ${countTotal}")
                if (uuid== userGet){
                    _hasilPrediksi.value = HasilPrediksi(
                        countTotal,
                        dataSent,
                        count1,
                        count2,
                        count3,
                        count4,
                        count5,
                        count6
                    )
                    uiState.update { it.copy(loadingState = false) }

                }else{
                    delay(500)
                    postData(dataSent,uuid)
                }
            }catch (e:Exception){
                Log.e(API_TEST,"API terima model $e")
                uiState.update { it.copy(loadingState = false, errorMessage = e.message) }
                // Handle any errors or retry logic here
            }

        }
    }

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://pkm.fewsstmkg.com/")
            .addConverterFactory(GsonConverterFactory.create(Gson()))
            .build()

        apiService = retrofit.create(ApiService::class.java)
    }
}

data class PrediksiPanenUIstate(
    val loadingState:Boolean = false,
    val errorMessage:String? = null
)