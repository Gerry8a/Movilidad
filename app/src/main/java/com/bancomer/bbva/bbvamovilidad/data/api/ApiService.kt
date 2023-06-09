package com.bancomer.bbva.bbvamovilidad.data.api

import com.bancomer.bbva.bbvamovilidad.data.api.movieResponse.MovieResponse
import com.bancomer.bbva.bbvamovilidad.data.api.request.CarbonPrintRequest
import com.bancomer.bbva.bbvamovilidad.data.api.request.UserUpdateWorkCenter
import com.bancomer.bbva.bbvamovilidad.data.api.response.CatalogResponse
import com.bancomer.bbva.bbvamovilidad.data.api.response.UserInfo
import com.bancomer.bbva.bbvamovilidad.data.api.response.UserInfoResponse
import com.bancomer.bbva.bbvamovilidad.data.api.response.UserList
import com.bancomer.bbva.bbvamovilidad.utils.Dictionary.BASE_URL_GATEWAY
import com.bancomer.bbva.bbvamovilidad.utils.Dictionary.GET_CATALOGS
import com.bancomer.bbva.bbvamovilidad.utils.Dictionary.GET_INFO_BY_USER
import com.bancomer.bbva.bbvamovilidad.utils.Dictionary.LIST_TAB_CARBON_PRINT_COLAB
import com.bancomer.bbva.bbvamovilidad.utils.Dictionary.REGISTER_CARBON_PRINT_COLAB
import com.bancomer.bbva.bbvamovilidad.utils.Dictionary.UPDATE_USER_ACCEPT_TERMS
import com.bancomer.bbva.bbvamovilidad.utils.Dictionary.UPDATE_WORK_CENTER
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*

private val headers = okhttp3.Headers.Builder()
    .add("Requestor", "gerardoalfredo.ochoa.contractor@bbva.com")
    .add("X-BBVA-AppId", "dev-my-application-bbva")
    .build()

private val loggerBody = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
private val loggerHeader = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.HEADERS)

private val okHttpClient = OkHttpClient
    .Builder().apply {
        addInterceptor(ApiServiceInterceptor)
        addInterceptor(loggerBody)
        addInterceptor(loggerHeader)
    }
    .build()

private val retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL_GATEWAY)
    .client(okHttpClient)
    .addConverterFactory(MoshiConverterFactory.create())
    .build()

interface ApiService {
    @GET(GET_CATALOGS)
    suspend fun getCatalogs(): CatalogResponse

    @POST(UPDATE_WORK_CENTER)
    suspend fun updateWorkCenter(@Body user: UserUpdateWorkCenter): Any

    @POST(GET_INFO_BY_USER)
    suspend fun getUserInfo(@Body userM: UserInfo): UserInfoResponse

    @POST(LIST_TAB_CARBON_PRINT_COLAB)
    suspend fun getListCarbonPrint(@Body userList: UserList): Any

    @POST(REGISTER_CARBON_PRINT_COLAB)
    suspend fun registerCarbonPrint(@Body carbonPrintRequest: CarbonPrintRequest): Any

    @POST(UPDATE_USER_ACCEPT_TERMS)
    suspend fun updateUserAcceptTerm(@Body userm: UserInfo): Any


















    @GET("movie/popular")
    suspend fun listPopularMovies(@Query("api_key") apiKey: String): MovieResponse



    @GET("movie/popular")
    suspend fun listPopularMoviesHeader(): MovieResponse

    @GET("movie/popular?api_key=72e2e780397bdc9eedd34c1c78c38ccd&language=en-US&page=1")
    suspend fun pelisPopulares(): MovieResponse


    @GET("movsostenible/movil/getMovilCatalogs")
    suspend fun getCatalogos(): CatalogResponse

    @GET("mov-sostenible/s/movsostenible/movil/getMovilCatalogs")
    suspend fun getMine(): CatalogResponse

    suspend fun getVic(): CatalogResponse



}

object CatalogApi{
    val retrofitService: ApiService by lazy{
        retrofit.create(ApiService::class.java)
    }
}