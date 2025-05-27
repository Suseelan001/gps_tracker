package com.locationReminder.model.apiUtil.interceptor

import com.locationReminder.model.localStorage.MySharedPreference
import okhttp3.Interceptor
import okhttp3.Response
import okio.IOException
import javax.inject.Inject

class HeaderInterceptor @Inject constructor(private val mySharedPreference: MySharedPreference): Interceptor{

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val modifiedHeader = request.newBuilder()


        modifiedHeader
            .addHeader("apikey", "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InJvbXZ2ZXVsZ29kZnBid3hqbWh5Iiwicm9sZSI6ImFub24iLCJpYXQiOjE3NDc5OTY4NzYsImV4cCI6MjA2MzU3Mjg3Nn0.4t24kl8UwTYsr5e23NRRttCcQbva9YYB1p4Ik7c54Hg")
            .addHeader(
                "Authorization",
                "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InJvbXZ2ZXVsZ29kZnBid3hqbWh5Iiwicm9sZSI6InNlcnZpY2Vfcm9sZSIsImlhdCI6MTc0Nzk5Njg3NiwiZXhwIjoyMDYzNTcyODc2fQ.NH1Us4q0JH4etpM5nfcIVe0UU_aOwKh43HD4mELhAB4"
            )

        return chain.proceed(modifiedHeader.build())
    }


}