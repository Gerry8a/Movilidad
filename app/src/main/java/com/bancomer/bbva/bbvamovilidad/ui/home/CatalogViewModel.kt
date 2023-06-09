package com.bancomer.bbva.bbvamovilidad.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bancomer.bbva.bbvamovilidad.data.api.ApiResponseStatus
import com.bancomer.bbva.bbvamovilidad.data.api.response.Data
import com.bancomer.bbva.bbvamovilidad.data.api.response.Medio
import com.bancomer.bbva.bbvamovilidad.data.local.entities.MedioEntity
import com.bancomer.bbva.bbvamovilidad.model.Movie
import com.bancomer.bbva.bbvamovilidad.repository.CatalogRepository
import com.bancomer.bbva.bbvamovilidad.ui.newtrip.DataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CatalogViewModel @Inject constructor(
    private val catalogRepository: CatalogRepository,
    private val dataRepository: DataRepository
) : ViewModel() {

//    private val catalogRepository = CatalogRepository()

    private val _catalog = MutableLiveData<ApiResponseStatus<Data>>()
    val catalog: LiveData<ApiResponseStatus<Data>> get() = _catalog

    private val _status = MutableLiveData<ApiResponseStatus<Data>>()
    val status: LiveData<ApiResponseStatus<Data>> get() = _status

    private val _statusMovie = MutableLiveData<ApiResponseStatus<List<Movie>>>()
    val statusMovie: LiveData<ApiResponseStatus<List<Movie>>> get() = _statusMovie

    private val _movie = MutableLiveData<List<Movie>>()
    val movie: LiveData<List<Movie>> get() = _movie


    init {
//        downloadMovies()
    }

    fun insertMedio(medio: Medio) = viewModelScope.launch {
        val medioEntity = MedioEntity(
            id = medio.id,
            asset1x = medio.asset1x,
            nomMedioTraslado = medio.nomMedioTraslado,
            numEmisionCo2e = medio.numEmisionCo2e,
            idSemaforo = medio.idSemaforo,
        )
        dataRepository.insertMedio(medioEntity)
    }

    fun downloadCatalog(){
        viewModelScope.launch {
            _status.value = ApiResponseStatus.Loading()
            handlerResponseStatus(catalogRepository.downloadCatalog())
        }
    }

    private fun handlerResponseStatus(apiResponseStatus: ApiResponseStatus<Data>) {
        if (apiResponseStatus is ApiResponseStatus.Success){
            _catalog.value = apiResponseStatus
        }
        _status.value = apiResponseStatus

    }

    private fun ggg(apiResponseStatus: ApiResponseStatus<List<Movie>>) {
        if (apiResponseStatus is ApiResponseStatus.Success){
            _movie.value = apiResponseStatus.data!!
        }
//        _status.value = apiResponseStatus
    }

    private fun downloadMovies() = viewModelScope.launch {
//        _status.value = ApiResponseStatus.Loading()
//            ggg(catalogRepository.downloadCatalog())
        ggg(catalogRepository.downloadMovie())
    }


//    private fun movieRepsonse(dowloadMovie: Movie?) {
//        _movie.value = dowloadMovie!!
//
//    }

    
}