package com.example.crmmainproject.model.apiUtil.utils


import org.json.JSONObject
import retrofit2.Response

open class BaseApiResponse {
    suspend fun <T> safeApiCall( apiCall: suspend () -> Response<T>): NetworkResult<T> {
        try {
            val response = apiCall()
            if (response.isSuccessful) {
                val body = response.body()
                body?.let {
                    return NetworkResult.Success(body)
                }
            }
            var message="Something went wrong!"
            response.errorBody()?.let {
                val data = JSONObject(it.string())
                if (data.has("error_msg")) {
                    message = data.getString("error_msg")
                }
                if (data.has("result")){
                    if (data.get("result") is JSONObject) {
                        val result = data.getJSONObject("result")
                         if (result.has("error_msg")) {
                            message = result.getString("error_msg")
                        }
                    }
                }
                if (data.has("data")){
                    if (data.get("data") is JSONObject){
                        val result = data.getJSONObject("data")
                        if (result.has("error_msg")) {
                            message = result.getString("error_msg")
                        }
                    }
                }

                if (data.has("statusCode")) {
                    val statusCode = data.getInt("statusCode")
                    if (statusCode == 400) {
                        if (data.has("message")) {
                            message = data.getString("message")
                        } else {
                            message = "An error occurred."
                        }
                    }
                }

            }
            return error(message)
        } catch (e: Exception) {
            return error(e.message ?: e.toString())
        }
    }
    private fun <T> error(errorMessage: String): NetworkResult<T> =
        NetworkResult.Error(errorMessage)
}