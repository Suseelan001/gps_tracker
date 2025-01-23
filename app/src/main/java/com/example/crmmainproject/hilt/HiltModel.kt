package com.example.crmmainproject.hilt

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class HiltModel {

    @Provides
    @Singleton
    fun providerContext(@ApplicationContext context: Context): Context = context


}
