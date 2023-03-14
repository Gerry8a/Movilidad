package com.bancomer.bbva.bbvamovilidad.data.api

import okhttp3.Interceptor
import okhttp3.Response

object ApiServiceInterceptor: Interceptor {

    private var sessionToken: String? = null

    fun setSessionToken(sessionToken: String){
        this.sessionToken = getAccessToken(sessionToken)
    }

    private fun getAccessToken(sessionToken: String): String? {
        val token = String.format("Bearer $sessionToken")
        return token

    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
            .newBuilder()
            .addHeader("Authorization", sessionToken!!)
            .addHeader("Content-Type", "application/json")
//            .addHeader("Requestor", "gerardoalfredo.ochoa.contractor@bbva.com")
//            .addHeader("X-BBVA-AppId", "58945098171-pe1cubts7c2sd3337isu1aakvtatj50m.apps.googleusercontent.com")
//            .addHeader("ggg", sessionToken!!)
//            ..addHeader("X-BBVA-AppId", "58945098171-pe1cubts7c2sd3337isu1aakvtatj50m.apps.googleusercontent.com")
            .build()
        return chain.proceed(request)
    }


}