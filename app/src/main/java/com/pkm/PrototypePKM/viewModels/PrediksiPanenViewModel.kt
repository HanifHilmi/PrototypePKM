package com.pkm.PrototypePKM.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.pkm.PrototypePKM.utils.API_TEST
import com.pkm.PrototypePKM.utils.ApiService
import com.pkm.PrototypePKM.utils.InAgro
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class PrediksiPanenViewModel:ViewModel() {



    private val _response = MutableStateFlow<InAgro?>(null)
    val postInAgro: StateFlow<InAgro?> = _response

    private val apiService: ApiService

    fun postData(requestData: InAgro) {

        val json = Json.encodeToString(requestData)
        Log.d(API_TEST,"json bdoy $json")
        val requestBody = json.toRequestBody("application/json".toMediaType())
        viewModelScope.launch {
//            try {
//                apiService.postInAgro(requestBody).enqueue(object: Callback<Unit> {
//                    override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
//                        Log.d(API_TEST,"API kirim InAgro ${response.code()} $response")
//                    }
//
//                    override fun onFailure(call: Call<Unit>, t: Throwable) {
//                        Log.e(API_TEST,"API Kirim Gagal $t")                }
//
//                })
//            }catch (e:Exception){
//                Log.e(API_TEST,"API Kirim Error $e")
//            }
            try {
                Log.d(API_TEST,"API kirim InAgro")
                val response = apiService.postInAgro(requestBody)
                Log.d(API_TEST,"API kirim InAgro hasil ${response.code()} ${response}")
                //_response.value = response.awaitResponse()
            } catch (e: Exception) {
                Log.e(API_TEST,"API kirim InAgro $e")

                // Error handling here
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

