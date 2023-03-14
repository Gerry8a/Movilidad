package com.bancomer.bbva.bbvamovilidad.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bancomer.bbva.bbvamovilidad.data.api.ApiResponseStatus
import com.bancomer.bbva.bbvamovilidad.data.api.response.DataX
import com.bancomer.bbva.bbvamovilidad.data.local.entities.UserEntity
import com.bancomer.bbva.bbvamovilidad.repository.CatalogRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val repository: CatalogRepository
) : ViewModel() {

    private val _status = MutableLiveData<ApiResponseStatus<Any>>()
    val status: LiveData<ApiResponseStatus<Any>> get() = _status


    init {
        getListUser()
        registerList()
    }

    private fun registerList() {
        viewModelScope.launch {
            _status.value = ApiResponseStatus.Loading()
            handleResponseStatus(repository.registerCarbon())
        }
    }

    fun insertUser(mail: String) {
        viewModelScope.launch {
            repository.getUserInfo(mail).let {
                when(it){
                    is ApiResponseStatus.Error -> TODO()
                    is ApiResponseStatus.Loading -> TODO()
                    is ApiResponseStatus.Success -> {
                        val response = it.data.data
                        val ttt = saveUserInfo(response)
                        repository.insertUser(ttt)
                    }
                }
            }
        }
    }

    private fun saveUserInfo(response: DataX): UserEntity {
        val userEntity = UserEntity()
        userEntity.nombres = response.nombres
        userEntity.userm = response.usuariom
        userEntity.codCentroTrabajo = response.codCentroTrabajo
        return userEntity
    }

    fun getUserInfo(userM: String) {
        viewModelScope.launch {
            _status.value = ApiResponseStatus.Loading()
//            handleResponseStatus(repository.getUserInfo(userM))
        }
    }

    fun getListUser() {
        viewModelScope.launch {
            _status.value = ApiResponseStatus.Loading()
            handleResponseStatus(repository.getListUser())
        }
    }

    private fun handleResponseStatus(apiResponseStatus: ApiResponseStatus<Any>) {
        _status.value = apiResponseStatus

    }
}