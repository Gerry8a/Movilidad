package com.bancomer.bbva.bbvamovilidad.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bancomer.bbva.bbvamovilidad.data.api.ApiResponseStatus
import com.bancomer.bbva.bbvamovilidad.data.api.response.DataX
import com.bancomer.bbva.bbvamovilidad.data.local.entities.UserEntity
import com.bancomer.bbva.bbvamovilidad.repository.CatalogRepository
import com.bancomer.bbva.bbvamovilidad.utils.Dictionary.PARQUES_POLANCO
import com.bancomer.bbva.bbvamovilidad.utils.Dictionary.PARQUES_POLANCO_ID
import com.bancomer.bbva.bbvamovilidad.utils.Dictionary.TORRE_BBVA
import com.bancomer.bbva.bbvamovilidad.utils.Dictionary.TORRE_BBVA_ID
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val repository: CatalogRepository
) : ViewModel() {

    private val _status = MutableLiveData<ApiResponseStatus<Any>>()
    val status: LiveData<ApiResponseStatus<Any>> get() = _status

    private lateinit var userM: String


    init {
//        downloadCatalog()
//        getListUser()
//        registerList()
    }

    private fun registerList() {
        viewModelScope.launch {
            _status.value = ApiResponseStatus.Loading()
            handleResponseStatus(repository.registerCarbon())
        }
    }

    private fun updateWorkCenter(campus: Int){
        viewModelScope.launch {
            _status.value = ApiResponseStatus.Loading()
            val userEntity = repository.getUserFromDB()
            userM = userEntity.userm!!
            handleResponseStatus(repository.updateCampus(campus, userM))
        }
    }

    /**
     * Función que guarda el usuario en el base de datos
     */
    // TODO: Agregar un UIState
    fun insertUser(mail: String) {
        viewModelScope.launch {
            repository.getUserInfo(mail).let {
                when(it){
                    is ApiResponseStatus.Error -> {

                    }
                    is ApiResponseStatus.Loading -> {

                    }
                    is ApiResponseStatus.Success -> {
                        val response = it.data.data
                        val user = saveUserInfo(response)
                        repository.insertUser(user)
                    }
                }
            }
        }
    }

    /**
     * Función que recupera el response y lo convierte en una entity
     */
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

    fun getWorkCode(workCenter: String) {
        when(workCenter){
            TORRE_BBVA -> {updateWorkCenter(TORRE_BBVA_ID)}
            PARQUES_POLANCO -> {updateWorkCenter(PARQUES_POLANCO_ID)}
        }
    }
}