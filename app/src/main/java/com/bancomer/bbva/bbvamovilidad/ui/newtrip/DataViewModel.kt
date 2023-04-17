package com.bancomer.bbva.bbvamovilidad.ui.newtrip

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bancomer.bbva.bbvamovilidad.data.api.ApiResponseStatus
import com.bancomer.bbva.bbvamovilidad.data.api.request.CarbonPrintRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DataViewModel @Inject constructor(
    private val repository: DataRepository
) : ViewModel() {


    private val _status = MutableLiveData<ApiResponseStatus<Any>>()
    val status: LiveData<ApiResponseStatus<Any>> get() = _status

    fun sendRequest(request: CarbonPrintRequest) {
        registerPrint(request)
    }

    private fun registerPrint(request: CarbonPrintRequest) = viewModelScope.launch {
        _status.value = ApiResponseStatus.Loading()
        handleResponseStatus(repository.registerCarbon(request))

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