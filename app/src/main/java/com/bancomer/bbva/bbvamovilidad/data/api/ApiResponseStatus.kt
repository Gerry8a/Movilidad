package com.bancomer.bbva.bbvamovilidad.data.api

sealed class ApiResponseStatus<T> {
    class Success<T>(val data: T): com.bancomer.bbva.bbvamovilidad.data.api.ApiResponseStatus<T>()
    class Loading<T>: com.bancomer.bbva.bbvamovilidad.data.api.ApiResponseStatus<T>()
    class Error<T>(val messageID: String): com.bancomer.bbva.bbvamovilidad.data.api.ApiResponseStatus<T>()
}