package com.pkm.PrototypePKM.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.pkm.PrototypePKM.utils.API_TEST
import com.pkm.PrototypePKM.utils.ApiService
import com.pkm.PrototypePKM.utils.InFeedback
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class FeedbackViewModel:ViewModel() {

    private val apiService: ApiService



    fun postData(requestData: InFeedback) {

        val json = Json.encodeToString(requestData)
        Log.d(API_TEST,"json bdoy $json")
        val requestBody = json.toRequestBody("application/json".toMediaType())
        viewModelScope.launch {
            try {
                Log.d(API_TEST,"API kirim InFeedback")
                val response = apiService.postInFeedback(requestBody)
                Log.d(API_TEST,"API kirim InFeedback hasil ${response.code()} ${response}")
            } catch (e: Exception) {
                // Error handling here
                Log.e(API_TEST,"API kirim InFeedback $e")
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

