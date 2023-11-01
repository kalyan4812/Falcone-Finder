package com.example.falcone_finder.business.data.utils

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

suspend fun <T> safeApiCall(
    dispatcher: CoroutineDispatcher = Dispatchers.IO,
    apiCall: suspend () -> T
): Result<T> {
    return withContext(dispatcher) {
        try {
            Result.success(apiCall.invoke())
        } catch (throwable: Throwable) {
            throwable.printStackTrace()
            return@withContext Result.failure(throwable)
        }
    }
}
