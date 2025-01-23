package com.example.crmmainproject.model.apiUtil.interceptor

import com.example.crmmainproject.model.localStorage.MySharedPreference
import okhttp3.Interceptor
import okhttp3.Response
import okio.IOException
import javax.inject.Inject

class HeaderInterceptor @Inject constructor(private val mySharedPreference: MySharedPreference): Interceptor{

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        return try {
            val modifiedHeader = request.newBuilder()

            val getToken = mySharedPreference.getAccessToken()
            modifiedHeader
                .addHeader("api-key", "787777e6")
                .addHeader("app-type","787e6e")
            if (getToken != null) {
                modifiedHeader
                  //  .addHeader("Authorization", getToken)
                    .addHeader("Authorization", mySharedPreference.getUserId()?:"")
            }
            chain.proceed(modifiedHeader.build())

        } catch (e: Exception) {
            throw IOException(e.message)
        }
    }

}