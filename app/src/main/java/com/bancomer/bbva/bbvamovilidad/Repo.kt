package com.bancomer.bbva.bbvamovilidad
//
//object Api {
//    var retrofit: Retrofit? = null
//    var okHttpClient: OkHttpClient? = null
//    fun getApi(token: String?): ApiService? {
//        if (Api.okHttpClient == null) {
//            Api.initOkHttp(token)
//        }
//        if (Api.retrofit == null) {
//            val gsonBuilder: GsonBuilder = GsonBuilder()
//            gsonBuilder.registerTypeAdapter(User::class.java, Deserializer())
//            Api.retrofit = Retrofit.Builder()
//                .baseUrl(Environment.URL_SERVICE_AUTH)
//                .client(Api.okHttpClient)
//                .addConverterFactory(GsonConverterFactory.create(gsonBuilder.create()))
//                .build()
//        }
//        return Api.retrofit?.create(ApiService::class.java)
//    }
//
//    private fun initOkHttp(token: String?) {
//        val REQUEST_TIMEOUT: Int = 60
//        val httpClient: OkHttpClient.Builder = OkHttpClient.Builder()
//            .addInterceptor(object : Interceptor {
//                @Throws(IOException::class)
//                public override fun intercept(chain: Interceptor.Chain): Response {
//                    val original: Request = chain.request()
//                    val request: Request = original.newBuilder()
//                        .header("Authorization", "Bearer " + token)
//                        .build()
//                    return chain.proceed(request)
//                }
//            })
//            .connectTimeout(REQUEST_TIMEOUT.toLong(), TimeUnit.SECONDS)
//            .readTimeout(REQUEST_TIMEOUT.toLong(), TimeUnit.SECONDS)
//            .writeTimeout(REQUEST_TIMEOUT.toLong(), TimeUnit.SECONDS)
//        val interceptor: HttpLoggingInterceptor? = HttpLoggingInterceptor()
//        interceptor!!.setLevel(HttpLoggingInterceptor.Level.BODY)
//        httpClient.addInterceptor(interceptor)
//        Api.okHttpClient = httpClient.build()
//    }
//
//    fun resetClient() {
//        Api.okHttpClient = null
//        Api.retrofit = null
//    }
//}