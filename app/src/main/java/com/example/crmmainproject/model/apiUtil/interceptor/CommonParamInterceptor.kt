package com.example.crmmainproject.model.apiUtil.interceptor

import android.content.Context
import android.provider.Settings
import okhttp3.Interceptor
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import okio.Buffer
import org.json.JSONObject
import java.io.IOException
import java.util.Locale
import java.util.TimeZone
import javax.inject.Inject

class CommonParamInterceptor @Inject constructor(private val context: Context): Interceptor{

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        return if (chain.request().method=="POST") {
            chain.proceed(requestWithUpdatedParameter(chain.request()))
        }else{
            chain.proceed(chain.request())
        }
    }

    private fun requestWithUpdatedParameter(req: Request): Request {
        val newRequest: Request
        val body = editBody(req)
        newRequest = req.newBuilder().method(req.method, body).build()
        return newRequest
    }

    private fun toRequestBody(content:String,contentType: MediaType?): RequestBody {
        return content
            .toRequestBody(contentType.toString().toMediaTypeOrNull())
    }

    private fun editBody(request: Request): RequestBody {
        val currentLocale: Locale =
            context.resources.configuration.locales[0]
        val buffer =  Buffer()
        request.body?.writeTo(buffer)
        val strOldBody = buffer.readUtf8() // String representation of the current request body
        buffer.clear()
        buffer.close()
        val jsonObject: JSONObject = if (strOldBody.isEmpty()){
            JSONObject()
        }else{
            JSONObject(strOldBody)
        }
        var strNewBody=""
        if (request.url.toString().contains("settings/basic/info/update") || request.url.toString().contains("settings/course/update")){
            strNewBody =jsonObject
                .put("deviceId", Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID))
                .put("language", Locale.getDefault().language)
                .put("userType","business")
                .put("appType","mobileapp")
                .put( "appName" , "courserev-business")
                .put("platform","android")
                .put("deviceName", android.os.Build.MODEL)
                .put("countryCode",currentLocale.country).toString()
        }else{
            strNewBody =jsonObject
                .put("deviceId", Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID))
                .put("language", Locale.getDefault().language)
                .put("userType","business")
                .put("appType","courserev-business")
                .put("app-type","mobileapp")
                .put("platform","android")
                .put("appName" , "courserev-business")
                .put("deviceName", android.os.Build.MODEL)
                .put("countryCode",currentLocale.country)
                .put("timeZone",TimeZone.getDefault().id).toString()
        }

        return toRequestBody(strNewBody, request.body?.contentType())
    }
}