package com.example.crmmainproject.model.apiUtil.serviceModel


import android.content.Context
import com.example.crmmainproject.model.localStorage.MySharedPreference
import com.example.crmmainproject.model.apiUtil.interceptor.CommonParamInterceptor
import com.example.crmmainproject.model.apiUtil.interceptor.ConnectivityInterceptor
import com.example.crmmainproject.model.apiUtil.interceptor.HeaderInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class AppHiltModule {

    @Provides
    @Named("CRM_API_LINK")
    @Singleton
    fun providerConnectedAccountApiLink(): String = "https/baselink"


    @Provides // Hilt provider created to Inject context
    @Named("CRM_CONTEXT")
    @Singleton
    fun providerContext(@ApplicationContext context: Context): Context = context

    @Provides
    @Named("CRM_SHARED_PREFERENCES")
    @Singleton
    fun provideSharedPreference(@Named("CRM_CONTEXT") context: Context): MySharedPreference = MySharedPreference(context)

    @Provides
    @Singleton
    @Named("CRM_RETROFIT")
    fun provideConnectedAccountRetrofit(
        @Named("CRM_CONTEXT") context: Context, @Named("CRM_API_LINK") apiLink: String,
        @Named("CRM_SHARED_PREFERENCES") mySharedPreference: MySharedPreference,
    ): Retrofit {
        val client = OkHttpClient.Builder()
            .connectTimeout(2, TimeUnit.MINUTES)
            .writeTimeout(2, TimeUnit.MINUTES)
            .readTimeout(2, TimeUnit.MINUTES)
            .addInterceptor(ConnectivityInterceptor(context))
            .addInterceptor(HeaderInterceptor(mySharedPreference = mySharedPreference))
            .addInterceptor(CommonParamInterceptor(context))
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .build()

        return Retrofit.Builder()
            .baseUrl(apiLink)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()  
    }

    @Provides
    @Singleton
    @Named("CRM_API_SERVICE")
    fun providerConnectedAccountApiService( @Named("CRM_RETROFIT") retrofit: Retrofit): ApiService = retrofit.create(
        ApiService::class.java)


    @Provides
    @Singleton
    fun providerConnectedAccountBaseNetworkSyncClass(
        @Named("CRM_API_SERVICE") apiService: ApiService,
        defaultDispatcher: CoroutineDispatcher,
    ):
            BaseNetworkSyncClass = BaseNetworkSyncClass(
        apiService = apiService,
        defaultDispatcher = defaultDispatcher
    )



}