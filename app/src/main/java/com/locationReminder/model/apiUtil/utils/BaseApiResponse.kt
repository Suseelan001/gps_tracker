package com.locationReminder.model.apiUtil.utils


import org.json.JSONObject
import retrofit2.Response

open class BaseApiResponse {
    suspend fun <T> safeApiCall(apiCall: suspend () -> Response<T>): NetworkResult<T> {
        return try {
            val response = apiCall()
            if (response.isSuccessful) {
                val body = response.body()
                if (response.code() == 204 || body != null) {
                    return NetworkResult.Success(body)
                }
            }

            var message = "Something went wrong!"
            response.errorBody()?.let {
                val data = JSONObject(it.string())
                if (data.has("code") && data.getString("code") == "23505") {
                    message = "Record already exist"
                }

                if (data.has("error_msg")) {
                    message = data.getString("error_msg")
                }
                if (data.has("result")) {
                    if (data.get("result") is JSONObject) {
                        val result = data.getJSONObject("result")
                        if (result.has("error_msg")) {
                            message = result.getString("error_msg")
                        }
                    }
                }
                if (data.has("data")) {
                    if (data.get("data") is JSONObject) {
                        val result = data.getJSONObject("data")
                        if (result.has("error_msg")) {
                            message = result.getString("error_msg")
                        }
                    }
                }
                if (data.has("statusCode")) {
                    val statusCode = data.getInt("statusCode")
                    if (statusCode == 400) {
                        message = data.optString("message", "An error occurred.")
                    }
                }
            }
            error(message)
        } catch (e: Exception) {
            error(e.message ?: e.toString())
        }
    }

    private fun <T> error(errorMessage: String): NetworkResult<T> =
        NetworkResult.Error(errorMessage)
}
