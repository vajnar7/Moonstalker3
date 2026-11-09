package com.vajnar.moonstalker3.processor

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object Commands {

    suspend fun cmdReset(): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                val response = CommandsApiService.instance.reset()
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