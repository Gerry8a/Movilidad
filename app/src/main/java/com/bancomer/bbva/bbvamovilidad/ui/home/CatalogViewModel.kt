package com.bancomer.bbva.bbvamovilidad.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bancomer.bbva.bbvamovilidad.R
import com.bancomer.bbva.bbvamovilidad.data.api.ApiResponseStatus
import com.bancomer.bbva.bbvamovilidad.data.api.response.Data
import com.bancomer.bbva.bbvamovilidad.model.Movie
import com.bancomer.bbva.bbvamovilidad.repository.CatalogRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CatalogViewModel @Inject constructor(
    private val catalogRepository: CatalogRepository
) : ViewModel() {

//    private val catalogRepository = CatalogRepository()

    private val _catalog = MutableLiveData<Data>()
    val catalog: LiveData<Data> get() = _catalog

    private val _status = MutableLiveData<com.bancomer.bbva.bbvamovilidad.data.api.ApiResponseStatus<Data>>()
    val status: LiveData<com.bancomer.bbva.bbvamovilidad.data.api.ApiResponseStatus<Data>> get() = _status

    private val _statusMovie = MutableLiveData<com.bancomer.bbva.bbvamovilidad.data.api.ApiResponseStatus<List<Movie>>>()
    val statusMovie: LiveData<com.bancomer.bbva.bbvamovilidad.data.api.ApiResponseStatus<List<Movie>>> get() = _statusMovie

    private val _movie = MutableLiveData<List<Movie>>()
    val movie: LiveData<List<Movie>> get() = _movie

    init {
//        downloadMovies()
    }

    fun downloadCatalog(){
        viewModelScope.launch {
            _status.value = com.bancomer.bbva.bbvamovilidad.data.api.ApiResponseStatus.Loading()
            handlerResponseStatus(catalogRepository.downloadCatalog())
        }
    }

    private fun handlerResponseStatus(apiResponseStatus: com.bancomer.bbva.bbvamovilidad.data.api.ApiResponseStatus<Data>) {
        if (apiResponseStatus is com.bancomer.bbva.bbvamovilidad.data.api.ApiResponseStatus.Success){
            _catalog.value = apiResponseStatus.data!!
        }
        _status.value = apiResponseStatus

    }

    private fun ggg(apiResponseStatus: com.bancomer.bbva.bbvamovilidad.data.api.ApiResponseStatus<List<Movie>>) {
        if (apiResponseStatus is com.bancomer.bbva.bbvamovilidad.data.api.ApiResponseStatus.Success){
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