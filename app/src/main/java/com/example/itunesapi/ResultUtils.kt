package com.example.itunesapi

import retrofit2.Response

sealed class Result<out R> {

    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val exception: Exception) : Result<Nothing>()
}

object NetworkErrorException: Exception()
class ServerErrorException(val errorCode: String?, message: String?): Exception(message)

suspend fun <T : Any?> safeApiCall(call: suspend () -> Result<T>): Result<T> {
    return try {
        call()
    } catch (e: Throwable) {
        e.printStackTrace()
        // An exception was thrown when calling the API
        Result.Error(NetworkErrorException)
    }
}

fun <T : Any> Response<T>.getResult(): Result<T> {
    return if (isSuccessful) {
        val body = body()
        if (body != null) {
            Result.Success(body)
        } else {
            Result.Error(ServerErrorException(code().toString(), "Unexpected response body: null"))
        }
    } else {
        Result.Error(ServerErrorException(code().toString(), "Response is not successful"))
    }
}