package com.locationReminder.model.apiUtil.serviceModel


import android.content.Context
import androidx.room.Room
import com.locationReminder.model.localStorage.MySharedPreference
import com.locationReminder.model.apiUtil.interceptor.ConnectivityInterceptor
import com.locationReminder.model.apiUtil.interceptor.HeaderInterceptor
import com.locationReminder.roomDatabase.RoomBaseSetup
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
    @Named("LOCATION_REMINDER_API_LINK")
    @Singleton
    fun providerConnectedAccountApiLink(): String = "https://romvveulgodfpbwxjmhy.supabase.co/rest/v1/"


    @Provides
    @Named("LOCATION_REMINDER_CONTEXT")
    @Singleton
    fun providerContext(@ApplicationContext context: Context): Context = context

    @Provides
    @Named("LOCATION_REMINDER_SHARED_PREFERENCES")
    @Singleton
    fun provideSharedPreference(@Named("LOCATION_REMINDER_CONTEXT") context: Context): MySharedPreference = MySharedPreference(context)

    @Provides
    @Singleton
    @Named("LOCATION_REMINDER_RETROFIT")
    fun provideConnectedAccountRetrofit(
        @Named("LOCATION_REMINDER_CONTEXT") context: Context, @Named("LOCATION_REMINDER_API_LINK") apiLink: String,
        @Named("LOCATION_REMINDER_SHARED_PREFERENCES") mySharedPreference: MySharedPreference,
    ): Retrofit {
        val client = OkHttpClient.Builder()
            .connectTimeout(2, TimeUnit.MINUTES)
            .writeTimeout(2, TimeUnit.MINUTES)
            .readTimeout(2, TimeUnit.MINUTES)
            .addInterceptor(ConnectivityInterceptor(context))
            .addInterceptor(HeaderInterceptor(mySharedPreference = mySharedPreference))
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
    @Named("LOCATION_REMINDER_API_SERVICE")
    fun providerConnectedAccountApiService( @Named("LOCATION_REMINDER_RETROFIT") retrofit: Retrofit): ApiService = retrofit.create(
        ApiService::class.java)


    @Provides
    @Singleton
    fun providerConnectedAccountBaseNetworkSyncClass(
        @Named("LOCATION_REMINDER_API_SERVICE") apiService: ApiService,
    ):
            BaseNetworkSyncClass = BaseNetworkSyncClass(
        apiService = apiService
    )





    @Provides
    @Singleton
    fun provide(@ApplicationContext context: Context) = Room.databaseBuilder(
        context, RoomBaseSetup::class.java, "LOCATION_REMINDER_database"
    )
        .allowMainThreadQueries()
        .fallbackToDestructiveMigration(false)
        .build()


   @Provides
    @Singleton
    fun provideLocationDao(roomBaseSetup: RoomBaseSetup) =roomBaseSetup.locationDAO()


   @Provides
    @Singleton
    fun provideContactDAO(roomBaseSetup: RoomBaseSetup) =roomBaseSetup.contactDAO()

   @Provides
    @Singleton
    fun provideSettingsDAO(roomBaseSetup: RoomBaseSetup) =roomBaseSetup.settingsDAO()

   @Provides
    @Singleton
    fun provideUserDao(roomBaseSetup: RoomBaseSetup) =roomBaseSetup.userDao()



}