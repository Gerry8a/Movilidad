package com.bancomer.bbva.bbvamovilidad.di

import android.content.Context
import com.bancomer.bbva.bbvamovilidad.data.api.ApiService
import com.bancomer.bbva.bbvamovilidad.utils.Dictionary
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

//    @Singleton
//    @Provides
//    fun provideInterceptor(context: Context): ApiServiceInterceptor = ApiServiceInterceptor(context)
//
//    @Provides
//    fun provideOkHttpClient(apiServiceInterceptor: ApiServiceInterceptor): OkHttpClient =
//        OkHttpClient.Builder()
//            .addInterceptor(apiServiceInterceptor)
//            .build()

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Dictionary.MOVIES)
//            .baseUrl("https://api.themoviedSíb.org/3/")
            .addConverterFactory(MoshiConverterFactory.create())
//            .client(okHttpClient)
            .build()
    }

    @Singleton
    @Provides
    fun provideApiClient(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }
}