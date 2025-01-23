package com.example.crmmainproject.model.apiUtil.interceptor

import android.content.Context
import com.example.crmmainproject.model.apiUtil.utils.NetworkUtil
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import javax.inject.Inject

class ConnectivityInterceptor @Inject constructor(private val context: Context): Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        if (!NetworkUtil.isOnline(context)) {
            throw NoConnectivityException()
        }
        val builder = chain.request().newBuilder()
        return chain.proceed(builder.build())
    }

}