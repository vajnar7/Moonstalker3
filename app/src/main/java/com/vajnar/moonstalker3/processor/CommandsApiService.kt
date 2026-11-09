package com.vajnar.moonstalker3.processor

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.POST

interface CommandsApiService {
    @POST("command/reset")
    suspend fun reset(): Response<Unit>

    companion object {
        // Static Base URL & Port for the server
        // "http://10.0.2.2:8080/" maps to localhost:8080 on your host machine from Android Emulator
        // Change to your server's static IP/Port if needed (e.g., "http://192.168.1.100:8080/")
        const val BASE_URL = "http://192.168.7.73:5000/"

        val instance: CommandsApiService by lazy {
            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(CommandsApiService::class.java)
        }
    }
}