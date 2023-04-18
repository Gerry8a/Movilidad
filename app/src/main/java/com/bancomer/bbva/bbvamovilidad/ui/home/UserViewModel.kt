package com.bancomer.bbva.bbvamovilidad.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bancomer.bbva.bbvamovilidad.data.UIState
import com.bancomer.bbva.bbvamovilidad.data.api.ApiResponseStatus
import com.bancomer.bbva.bbvamovilidad.data.api.response.DataX
import com.bancomer.bbva.bbvamovilidad.data.local.entities.UserEntity
import com.bancomer.bbva.bbvamovilidad.repository.CatalogRepository
import com.bancomer.bbva.bbvamovilidad.utils.Dictionary.PARQUES_POLANCO
import com.bancomer.bbva.bbvamovilidad.utils.Dictionary.PARQUES_POLANCO_ID
import com.bancomer.bbva.bbvamovilidad.utils.Dictionary.TAG
import com.bancomer.bbva.bbvamovilidad.utils.Dictionary.TORRE_BBVA
import com.bancomer.bbva.bbvamovilidad.utils.Dictionary.TORRE_BBVA_ID
import com.bancomer.bbva.bbvamovilidad.utils.Dictionary.USERM
import com.bancomer.bbva.bbvamovilidad.utils.Dictionary.USER_ACCEPT_TERM
import com.bancomer.bbva.bbvamovilidad.utils.Preferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val repository: CatalogRepository,
    private val preferences: Preferences
) : ViewModel() {

    private val _status = MutableLiveData<ApiResponseStatus<Any>>()
    val status: LiveData<ApiResponseStatus<Any>> get() = _status

    private val _userInfo = MutableLiveData<UIState<UserEntity>>()
    val userInfo: LiveData<UIState<UserEntity>> get() = _userInfo

    private lateinit var userM: String

    init {
//        getUserInfoFromDB()
//        downloadCatalog()
//        getListUser()
//        registerList()
    }

    fun registerrrr() = viewModelScope.launch {
        _status.value = ApiResponseStatus.Loading()
        handleResponseStatus(repository.registerCarbon())

    }

    fun getUserInfoFromDB() = viewModelScope.launch {
        val userData = repository.getUserFromDB()
        if (userData != null) {
            _userInfo.value = UIState.Success(userData)
        }
    }

    fun updateUserAcceptTerm(userM: String) = viewModelScope.launch {
        _status.value = ApiResponseStatus.Loading()
        handleResponseStatus(repository.updateUserAcceptTerm(userM))
    }



    private fun updateWorkCenter(campus: Int) {
        viewModelScope.launch {
            _status.value = ApiResponseStatus.Loading()
            val userEntity = repository.getUserFromDB()
            userM = "XMF0673"
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
                when (it) {
                    is ApiResponseStatus.Error -> {
                        Log.d(TAG, "insertUserVIEWMODEL: REGRESA ERROR")
                    }
                    is ApiResponseStatus.Loading -> {

                    }
                    is ApiResponseStatus.Success -> {
                        val response = it.data.data
                        val user = saveUserInfo(response)
                        Log.d(TAG, "insertUserVIEWMODEL: SE GUARDÓ")
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
        userEntity.fhAceptaTerminos = response.fhAceptaTerminos
        userEntity.centroTrabajoAct = response.centroTrabajoAct
        preferences.save(USERM, response.usuariom)

        if (response.fhAceptaTerminos != null) {
            preferences.save(USER_ACCEPT_TERM, true)
        } else {
            preferences.save(USER_ACCEPT_TERM, false)
        }
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
        when (workCenter) {
            TORRE_BBVA -> {
                updateWorkCenter(TORRE_BBVA_ID)
                updateWorkCenterDB(TORRE_BBVA_ID, workCenter)
            }
            PARQUES_POLANCO -> {
                updateWorkCenter(PARQUES_POLANCO_ID)
                updateWorkCenterDB(PARQUES_POLANCO_ID, workCenter)
            }
        }
    }

    private fun updateWorkCenterDB(codWorkCenter: Int, workCenter: String) = viewModelScope.launch {
        repository.upDateWorkCenterDB(codWorkCenter, workCenter)
    }

}

