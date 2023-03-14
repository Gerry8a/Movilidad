package com.bancomer.bbva.bbvamovilidad.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.bancomer.bbva.bbvamovilidad.utils.Dictionary.PREFERENCES
import com.bancomer.bbva.bbvamovilidad.utils.Preferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideContext(application: Application): Context {
        return application
    }

    @Provides
    fun provideSharedPreferences(context: Context): SharedPreferences =
        context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE)

    @Singleton
    @Provides
    fun providePreferences(sharedPreferences: SharedPreferences): Preferences =
        Preferences(sharedPreferences)
}