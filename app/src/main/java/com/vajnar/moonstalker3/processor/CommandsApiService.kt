package com.vajnar.moonstalker3.processor

import com.google.gson.annotations.SerializedName
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

data class PingResponse(
    @SerializedName("success") val success: Boolean = false,
    @SerializedName("message") val message: String = "",
    @SerializedName("state") val state: String = "",
    @SerializedName("error_data") val errorData: String = "",
    @SerializedName("data") val data: String = "",
    @SerializedName("warning") val warning: String = "",
    @SerializedName("info") val info: String = "",
)

data class LoginRequest(
    val username: String,
    val password: String,
)

data class LoginResponse(
    @SerializedName("token") val token: String? = null,
    @SerializedName("access_token") val accessToken: String? = null
) {
    val authToken: String
        get() = token ?: accessToken ?: ""
}

interface CommandsApiService {

    @POST("login")
    suspend fun login(
        @Body request: LoginRequest
    ): Response<LoginResponse>

    suspend fun login(
        username: String,
        password: String
    ): Response<LoginResponse> = login(LoginRequest(username, password))

    @POST("command/reset")
    suspend fun reset(
        @Header("Authorization") token: String? = null
    ): Response<Unit>

    @POST("command/connect")
    suspend fun connect(
        @Header("Authorization") token: String? = null
    ): Response<Unit>

    @POST("command/ping")
    suspend fun ping(
        @Header("Authorization") token: String? = null
    ): Response<PingResponse>

    @POST("command/move_start")
    suspend fun moveStart(
        @retrofit2.http.Query("direction") direction: String,
        @Header("Authorization") token: String? = null
    ): Response<Unit>

    @POST("command/move_stop")
    suspend fun moveStop(
        @Header("Authorization") token: String? = null
    ): Response<Unit>

    companion object {
        // Static Base URL & Port for the server
        // "http://10.0.2.2:8080/" maps to localhost:8080 on your host machine from Android Emulator
        // Change to your server's static IP/Port if needed (e.g., "http://192.168.1.100:8080/")
        const val BASE_URL = "http://192.168.77.8:5000/"

        var token: String? = null

        private val okHttpClient: OkHttpClient by lazy {
            OkHttpClient.Builder()
                .addInterceptor { chain ->
                    val originalRequest = chain.request()
                    val currentToken = token
                    val requestBuilder = originalRequest.newBuilder()
                    if ((!currentToken.isNullOrEmpty()) && (originalRequest.header("Authorization") == null)) {
                        val authHeaderValue = if (currentToken.startsWith("Bearer ", ignoreCase = true)) {
                            currentToken
                        } else {
                            "Bearer $currentToken"
                        }
                        requestBuilder.header("Authorization", authHeaderValue)
                    }
                    chain.proceed(requestBuilder.build())
                }
                .build()
        }

        val instance: CommandsApiService by lazy {
            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(CommandsApiService::class.java)
        }
    }
}