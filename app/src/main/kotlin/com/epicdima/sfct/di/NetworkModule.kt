package com.epicdima.sfct.di

import android.app.Application
import com.epicdima.sfct.core.network.ApiService
import com.epicdima.sfct.network.RetrofitApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

/**
 * @author EpicDima
 */
@InstallIn(SingletonComponent::class)
@Module
object NetworkModule {

    @Singleton
    @Provides
    fun provideRetrofitService(retrofit: Retrofit): ApiService =
        retrofit.create(RetrofitApiService::class.java)

    @Singleton
    @Provides
    fun provideOkHttpClient(application: Application): OkHttpClient = OkHttpClient.Builder()
        .cache(Cache(application.cacheDir, 10L * 1024 * 1024))
        .connectTimeout(5, TimeUnit.SECONDS)
        .readTimeout(8, TimeUnit.SECONDS)
        .build()

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
        .baseUrl(RetrofitApiService.BASE_URL)
        .validateEagerly(true)
        .client(okHttpClient)
        .addConverterFactory(ScalarsConverterFactory.create())
        .build()
}