package com.vajnar.moonstalker3.processor

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object Commands {

    suspend fun cmdLogin(username: String, password: String): Result<LoginResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val response = CommandsApiService.instance.login(username, password)
                if (response.isSuccessful) {
                    val body = response.body()
                    val token = body?.authToken
                    if (!token.isNullOrEmpty()) {
                        CommandsApiService.token = token
                    }
                    Result.success(body ?: LoginResponse())
                } else {
                    Result.failure(Exception("Server Error ${response.code()}: ${response.message()}"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    suspend fun cmdReset(token: String? = null): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                val response = if (token != null) {
                    CommandsApiService.instance.reset(token = if (token.startsWith("Bearer ", ignoreCase = true)) token else "Bearer $token")
                } else {
                    CommandsApiService.instance.reset()
                }
                if (response.isSuccessful) {
                    Result.success(Unit)
                } else {
                    Result.failure(Exception("Server Error ${response.code()}: ${response.message()}"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    suspend fun cmdConnect(token: String? = null): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                val response = if (token != null) {
                    CommandsApiService.instance.connect(token = if (token.startsWith("Bearer ", ignoreCase = true)) token else "Bearer $token")
                } else {
                    CommandsApiService.instance.connect()
                }
                if (response.isSuccessful) {
                    Result.success(Unit)
                } else {
                    Result.failure(Exception("Server Error ${response.code()}: ${response.message()}"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    suspend fun cmdPing(token: String? = null): Result<PingResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val response = if (token != null) {
                    CommandsApiService.instance.ping(token = if (token.startsWith("Bearer ", ignoreCase = true)) token else "Bearer $token")
                } else {
                    CommandsApiService.instance.ping()
                }
                if (response.isSuccessful) {
                    val body = response.body() ?: PingResponse()
                    Result.success(body)
                } else {
                    Result.failure(Exception("Server Error ${response.code()}: ${response.message()}"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    suspend fun cmdMoveStart(direction: String, token: String? = null): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                val response = if (token != null) {
                    CommandsApiService.instance.moveStart(direction, token = if (token.startsWith("Bearer ", ignoreCase = true)) token else "Bearer $token")
                } else {
                    CommandsApiService.instance.moveStart(direction)
                }
                if (response.isSuccessful) {
                    Result.success(Unit)
                } else {
                    Result.failure(Exception("Server Error ${response.code()}: ${response.message()}"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    suspend fun cmdMoveStop(token: String? = null): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                val response = if (token != null) {
                    CommandsApiService.instance.moveStop(token = if (token.startsWith("Bearer ", ignoreCase = true)) token else "Bearer $token")
                } else {
                    CommandsApiService.instance.moveStop()
                }
                if (response.isSuccessful) {
                    Result.success(Unit)
                } else {
                    Result.failure(Exception("Server Error ${response.code()}: ${response.message()}"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

}