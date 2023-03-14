package com.bancomer.bbva.bbvamovilidad.di

import android.content.Context
import androidx.room.Room
import com.bancomer.bbva.bbvamovilidad.data.local.UserDatabase
import com.bancomer.bbva.bbvamovilidad.utils.Dictionary.USER_DATABASE
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {


    @Singleton
    @Provides
    fun provideRoom(@ApplicationContext context: Context) = Room.databaseBuilder(
        context, UserDatabase::class.java, USER_DATABASE
    ).build()

    @Singleton
    @Provides
    fun provideUserDao(db: UserDatabase) = db.getUserDao()
}