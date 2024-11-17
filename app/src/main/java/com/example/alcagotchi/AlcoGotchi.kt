package com.example.alcagotchi

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException

class AlcoGotchi private constructor() {

    private val client = OkHttpClient()
    private val jsonMediaType = "application/json; charset=utf-8".toMediaType()

    var baseUrl = "http://192.168.4.1:80"
    var coins = 100

    companion object {
        @Volatile private var instance: AlcoGotchi? = null

        fun getInstance() =
            instance ?: synchronized(this) {
                instance ?: AlcoGotchi().also { instance = it }
            }
    }

    private fun buildUrl(path: String): String {
        return "$baseUrl/$path"
    }

    val basicCallback = object : Callback {
        override fun onFailure(call: Call, e: IOException) {
            e.printStackTrace()
        }

        override fun onResponse(call: Call, response: Response) {
            response.use {
                handleStateResponse(response)
            }
        }
    }

    fun handleStateResponse(response: Response) {
        if (!response.isSuccessful) throw IOException("Unexpected code $response")

        val json = JSONObject(response.body!!.string())

        coins = json.getJSONObject("data").getInt("alco_coin")
    }

    fun getState() {
        val request = Request.Builder()
            .url(buildUrl(""))
            .build()

        client.newCall(request).enqueue(basicCallback)
    }

    suspend fun postGamble(amount: Int) {
        val body = JSONObject()
        body.put("bet", amount)

        val request = Request.Builder()
            .url(buildUrl("gamble"))
            .post(body.toString().toRequestBody(jsonMediaType))
            .build()

        return withContext(Dispatchers.IO) {
            handleStateResponse(client.newCall(request).execute())
        }
    }
}
