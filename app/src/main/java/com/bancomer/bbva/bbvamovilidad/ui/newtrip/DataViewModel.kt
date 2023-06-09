package com.bancomer.bbva.bbvamovilidad.ui.newtrip

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bancomer.bbva.bbvamovilidad.data.UIState
import com.bancomer.bbva.bbvamovilidad.data.api.ApiResponseStatus
import com.bancomer.bbva.bbvamovilidad.data.api.request.CarbonPrintRequest
import com.bancomer.bbva.bbvamovilidad.data.api.response.Medio
import com.bancomer.bbva.bbvamovilidad.data.local.entities.MedioEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DataViewModel @Inject constructor(
    private val repository: DataRepository
) : ViewModel() {


    private val _status = MutableLiveData<ApiResponseStatus<Any>>()
    val status: LiveData<ApiResponseStatus<Any>> get() = _status

    private val _listMedio = MutableLiveData<UIState<MutableList<MedioEntity>>>()
    val listMedio: LiveData<UIState<MutableList<MedioEntity>>> get() = _listMedio

    fun sendRequest(request: CarbonPrintRequest) {
        registerPrint(request)
    }

    private fun registerPrint(request: CarbonPrintRequest) = viewModelScope.launch {
        _status.value = ApiResponseStatus.Loading()
        handleResponseStatus(repository.registerCarbon(request))
    }

//    fun saveMedio(medio: Medio) {
//        saveMedioDB(medio)
//    }

    fun getMedios() = viewModelScope.launch {
        repository.getListMedios().let {
            if (it.isNotEmpty()) {
                _listMedio.value = UIState.Success(it)
            }
        }
    }

    fun deleteMedios() = viewModelScope.launch {
        repository.deleteMedios()
    }


    private fun saveMedioDB(medio: Medio) = viewModelScope.launch {
        val medioEntity = MedioEntity(
            id = medio.id,
            asset1x = medio.asset1x,
            nomMedioTraslado = medio.nomMedioTraslado,
            numEmisionCo2e = medio.numEmisionCo2e,
            idSemaforo = medio.idSemaforo,
        )
        repository.insertMedio(medioEntity)
    }


    private fun registerList() {
        viewModelScope.launch {
            _status.value = ApiResponseStatus.Loading()
//            handleResponseStatus(repository.registerCarbon())
        }
    }

    private fun handleResponseStatus(apiResponseStatus: ApiResponseStatus<Any>) {
        _status.value = apiResponseStatus
    }

}