package com.bancomer.bbva.bbvamovilidad.data.api.response

import com.bancomer.bbva.bbvamovilidad.R
import com.bancomer.bbva.bbvamovilidad.data.api.ApiResponseStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.UnknownHostException

suspend fun <T> makeNetworkcall(
    call: suspend () -> T
): com.bancomer.bbva.bbvamovilidad.data.api.ApiResponseStatus<T> = withContext(Dispatchers.IO){
    try {
        com.bancomer.bbva.bbvamovilidad.data.api.ApiResponseStatus.Success(call())
    } catch (e: UnknownHostException){
        com.bancomer.bbva.bbvamovilidad.data.api.ApiResponseStatus.Error(e.message!!)
    } catch (e: Exception){
        com.bancomer.bbva.bbvamovilidad.data.api.ApiResponseStatus.Error(e.message!!)
    }
}