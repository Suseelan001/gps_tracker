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

                when (data.optString("code")) {
                    "23505" -> message = "Record already exists"
                    "45001" -> message = "Email not found"
                    "45002" -> message = "Invalid password"
                    "45003" -> message = "Email already registered"
                }

                message = data.optString("error_msg", message)
                message = data.optString("message", message)

                if (data.has("result") && data.get("result") is JSONObject) {
                    val result = data.getJSONObject("result")
                    message = result.optString("error_msg", message)
                }

                if (data.has("data") && data.get("data") is JSONObject) {
                    val result = data.getJSONObject("data")
                    message = result.optString("error_msg", message)
                }

                // HTTP status code based override (optional)
                if (data.optInt("statusCode") == 400) {
                    message = data.optString("message", message)
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
