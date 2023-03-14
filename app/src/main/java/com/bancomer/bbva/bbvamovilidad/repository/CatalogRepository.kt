package com.bancomer.bbva.bbvamovilidad.repository

import android.util.Log
import com.bancomer.bbva.bbvamovilidad.data.api.ApiResponseStatus
import com.bancomer.bbva.bbvamovilidad.data.api.CatalogApi.retrofitService
import com.bancomer.bbva.bbvamovilidad.data.api.dto.MovieDTOMapper
import com.bancomer.bbva.bbvamovilidad.data.api.request.CarbonPrintRequest
import com.bancomer.bbva.bbvamovilidad.data.api.request.Detalle
import com.bancomer.bbva.bbvamovilidad.data.api.response.*
import com.bancomer.bbva.bbvamovilidad.data.local.dao.UserDao
import com.bancomer.bbva.bbvamovilidad.data.local.entities.UserEntity
import com.bancomer.bbva.bbvamovilidad.model.Movie
import com.bancomer.bbva.bbvamovilidad.utils.Dictionary.TAG
import com.google.gson.Gson
import javax.inject.Inject

class CatalogRepository @Inject constructor(
//    private val apiService: ApiService
    private val userDao: UserDao
) {

    suspend fun insertUser(userEntity: UserEntity) =
        userDao.insertUser(userEntity)



    suspend fun downloadCatalog(): ApiResponseStatus<Data> =
        makeNetworkcall {
            val listCatalog = retrofitService.getCatalogs()
            listCatalog.data
        }

    suspend fun getUserInfo(email: String): ApiResponseStatus<UserInfoResponse> =
        makeNetworkcall {
            val user = UserInfo("", email)
            val listUserInfo = retrofitService.getUserInfo(user)
            listUserInfo
        }

    suspend fun getListUser(): ApiResponseStatus<Any> =
        makeNetworkcall {
            val userList = UserList("XME7841", "victorhugo.santillan.contractor@dev.bbva.com", 1)
            val listUser = retrofitService.getListCarbonPrint(userList)
            listUser
        }

    suspend fun registerCarbon(): ApiResponseStatus<Any> =
        makeNetworkcall {
            val detalle = Detalle(
                "2023-02-23T02:02:01.000Z",
                "2023-02-23T01:08:01.000Z",
                1,
                3,
                19.4981495,
                -99.2094496,
                12.4981495,
                -95.2094496
            )

            val ggg = listOf<Detalle>(detalle)

            val carbonPrintRequest = CarbonPrintRequest(
                29803,
                "55000",
                ggg,
                "victorhugo.santillan.contractor@dev.bbva.com",
                true
            )

            val jsonStirng = Gson().toJson(carbonPrintRequest)
            Log.d(TAG, jsonStirng)

            val listRegistro = retrofitService.registerCarbonPrint(carbonPrintRequest)
            carbonPrintRequest

        }


    suspend fun downloadMovie(): com.bancomer.bbva.bbvamovilidad.data.api.ApiResponseStatus<List<Movie>> =
        makeNetworkcall {
            val listMovie = retrofitService.listPopularMovies("72e2e780397bdc9eedd34c1c78c38ccd")
            val movieDTOList = listMovie.results
            val movieDTOMapper = MovieDTOMapper()
            val ttt = movieDTOMapper.fromMovieDTOListToMovieDomainList(movieDTOList)
            for (i in ttt) {
                Log.d(TAG, "downloadMovie: ${i.title}")
            }
            movieDTOMapper.fromMovieDTOListToMovieDomainList(movieDTOList)
        }
}



