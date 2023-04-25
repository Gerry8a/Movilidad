package com.bancomer.bbva.bbvamovilidad.ui.newtrip

import com.bancomer.bbva.bbvamovilidad.data.UIState
import com.bancomer.bbva.bbvamovilidad.data.api.ApiResponseStatus
import com.bancomer.bbva.bbvamovilidad.data.api.CatalogApi.retrofitService
import com.bancomer.bbva.bbvamovilidad.data.api.request.CarbonPrintRequest
import com.bancomer.bbva.bbvamovilidad.data.api.response.Medio
import com.bancomer.bbva.bbvamovilidad.data.api.response.makeNetworkcall
import com.bancomer.bbva.bbvamovilidad.data.local.dao.MedioDao
import com.bancomer.bbva.bbvamovilidad.data.local.entities.MedioEntity
import javax.inject.Inject

class DataRepository @Inject constructor(
    private val medioDao: MedioDao
) {

    suspend fun registerCarbon(request: CarbonPrintRequest): ApiResponseStatus<Any> =
        makeNetworkcall {
            retrofitService.registerCarbonPrint(request)
        }

    suspend fun insertMedio(medioEntity: MedioEntity) = medioDao.insertMedio(medioEntity)

    suspend fun getListMedios() : MutableList<MedioEntity> = medioDao.getListMedios()

    suspend fun deleteMedios() = medioDao.deleteMedio()
}