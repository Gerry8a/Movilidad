package com.bancomer.bbva.bbvamovilidad.ui.newtrip

import com.bancomer.bbva.bbvamovilidad.data.api.ApiResponseStatus
import com.bancomer.bbva.bbvamovilidad.data.api.CatalogApi.retrofitService
import com.bancomer.bbva.bbvamovilidad.data.api.request.CarbonPrintRequest
import com.bancomer.bbva.bbvamovilidad.data.api.response.makeNetworkcall
import javax.inject.Inject

class DataRepository @Inject constructor() {

    suspend fun registerCarbon(request: CarbonPrintRequest): ApiResponseStatus<Any> =
        makeNetworkcall {
            val listRegistro = retrofitService.registerCarbonPrint(request)
        }
}