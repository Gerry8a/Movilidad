package com.bancomer.bbva.bbvamovilidad.data.api

import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object MovieDBclient {

    private val retorfit = Retrofit.Builder()
        .baseUrl("https://api.themoviedb.org/3/")
        .addConverterFactory(MoshiConverterFactory.create())
        .build()

    val service = retorfit.create(ApiService::class.java)
}