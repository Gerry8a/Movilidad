package com.bancomer.bbva.bbvamovilidad

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.bancomer.bbva.bbvamovilidad.Environment.URL_SERVICE
import com.bancomer.bbva.bbvamovilidad.databinding.TrackingMainBinding
import com.bancomer.bbva.bbvamovilidad.ui.base.BaseActivity
import com.bancomer.bbva.bbvamovilidad.ui.onboarding.OnboardingActivity
import com.bancomer.bbva.bbvamovilidad.utils.Dictionary.ONBOARDING_FINISHED
import com.bbva.login.EnvironmentType
import com.bbva.login.ErrorCode
import com.bbva.login.OAuthManager
import com.bbva.login.OAuthManager.LogoutListener
import com.bbva.login.OAuthManager.RequestAccessTokenListener
import com.google.gson.GsonBuilder
import com.mb3364.http.AsyncHttpClient
import com.mb3364.http.HttpClient
import com.mb3364.http.RequestParams
import com.mb3364.http.StringHttpResponseHandler
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TrackingActivity : BaseActivity() {

    private lateinit var binding: TrackingMainBinding
    private var observableData = ObservableData()
    val TAG = "ggg"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val screenSplash = installSplashScreen()
        binding = TrackingMainBinding.inflate(layoutInflater)
        setContentView(binding.root)




        observableData.resultService = ""
        observableData.userLogin = ""
        observableData.accesstoken = ""
        observableData.registerNotifier = ""

        binding.btn.setOnClickListener {
            observableData.resultService = ""
            CallServiceTest("param1", "param2")
//            LogoutUser()
        }

        binding.btnLogOut.setOnClickListener { LogoutUser() }
        binding.btnMaps.setOnClickListener {
            val intent = Intent(this, MapsActivity::class.java)
            startActivity(intent)

        }

        binding.btnOnboarding.setOnClickListener {
            val intent = Intent(this, OnboardingActivity::class.java)
            startActivity(intent)
        }

        binding.btnRestart.setOnClickListener {
            preferences.save(ONBOARDING_FINISHED, false)
        }

        binding.tvOnboarding.text = preferences.get(ONBOARDING_FINISHED, false).toString()


        OAuthManager.getInstance().Configure(EnvironmentType.DEV)
//        OAuthManager.getInstance().Environment(Environment.Environment)
    }

    override fun onResume() {
        super.onResume()

        // OAuth Initial Check
        //------------------------------------------
        // Login al iniciar aplicación
        observableData.setAccesstoken("")
        observableData.setUserLogin("")
        val context: Context = this@TrackingActivity
        OAuthManager.getInstance().requestAccessToken(context, object : RequestAccessTokenListener {
            override fun onAccessTokenSuccess(token: String) {
                // Actualizar datos Tabs
                val gson = GsonBuilder().setPrettyPrinting().create() //new Gson();
                observableData.setAccesstoken(token)
                observableData.setUserLogin(
                    gson.toJson(
                        OAuthManager.getInstance().getUser(context)
                    )
                )
                binding.tvToken.text = token
                val user = gson.toJson(
                    OAuthManager.getInstance().getUser(context)
                )
                println("token: $token")
                println("user: $user")
                binding.tvUser.text = gson.toJson(
                    OAuthManager.getInstance().getUser(context)
                )
            }

            override fun onAccessTokenError(errorCode: Int, errorMessage: String) {
                OAuthErrorHandler(errorCode, errorMessage)
                // Actualizar datos Tabs
                observableData.setAccesstoken("Error OAuth: $errorCode $errorMessage")
                observableData.setUserLogin("")
                binding.tvToken.text = errorMessage
            }
        })
    }

    private fun callServiceTest(param1: String, param2: String) {

        // Ejemplo llamada servicio (en este caso UserInfo)


        val params = RequestParams() // <- parametros de ejemplo

        params.put("param1", param1)
        params.put("param2", param2)


        val client: HttpClient = AsyncHttpClient()
        client.userAgent = "Android client example"
        client.setHeader("Content-Type", "application/json")



        OAuthManager.getInstance().requestAccessToken(this@TrackingActivity, object :
            OAuthManager.RequestAccessTokenListener {
            override fun onAccessTokenSuccess(token: String?) {
                Log.d("ggg", "onAccessTokenSuccess: $token")
                client.setHeader("Authorization", "Bearer $token")
                observableData.accesstoken = token
                client.post(URL_SERVICE, params, object : StringHttpResponseHandler() {
                    override fun onSuccess(
                        statusCode: Int,
                        p1: MutableMap<String, MutableList<String>>?,
                        result: String?
                    ) {
                        runOnUiThread {
                            observableData.resultService = result
                        }
                    }

                    override fun onFailure(
                        statusCode: Int,
                        p1: MutableMap<String, MutableList<String>>?,
                        content: String?
                    ) {
                        runOnUiThread {
                            Log.d("ggg", "Fail: $token $statusCode")
                            observableData.resultService =
                                "Error Http status code $statusCode\n$content"
                        }
                    }

                    override fun onFailure(p0: Throwable?) {
                        runOnUiThread {
                            observableData.resultService = p0?.message
                        }
                    }
                })
            }

            override fun onAccessTokenError(p0: Int, p1: String?) {
                observableData.accesstoken = p1
            }

        })
    }

    fun CallServiceTest(param1: String?, param2: String?) {
        // Ejemplo llamada servicio (en este caso UserInfo)
        val params = RequestParams() // <- parametros de ejemplo
        params.put("param1", param1)
        params.put("param2", param2)
        val client: HttpClient = AsyncHttpClient()
        client.userAgent = "Android client example"
        client.setHeader("Content-Type", "application/json")
        // Refrescar token antes de cada invocación a un servicio
        OAuthManager.getInstance()
            .requestAccessToken(this@TrackingActivity, object : RequestAccessTokenListener {
                override fun onAccessTokenSuccess(token: String) {
                    // Establecer access token
                    Log.d(TAG, "Header token: $token")
                    client.setHeader("Authorization", "Bearer $token") // <--- Token
                    // Actualizar datos Tabs
                    observableData.setAccesstoken(token)
                    // POST request  --> Debería ser GET pero GCP lo tienen mal publicado
                    client.post(URL_SERVICE, params, object : StringHttpResponseHandler() {
                        override fun onSuccess(
                            statusCode: Int,
                            headers: Map<String, List<String>>,
                            result: String
                        ) {
                            Log.d(TAG, "Result: $result")
                            // RunOnUiThread  (Post se ejecuta en otro thread)
                            runOnUiThread { // Actualizar contenidos Tabs
                                observableData.setResultService(result)
                                binding.tvToken.text = result
                            }
                        }

                        override fun onFailure(
                            statusCode: Int,
                            headers: Map<String, List<String>>,
                            content: String
                        ) {
                            /* Server responded with a status code 4xx or 5xx error */
                            Log.e(
                                TAG,
                                "Error Http status code $statusCode\n$content"
                            )
                            // RunOnUiThread  (Post se ejecuta en otro thread)
                            runOnUiThread {
                                Toast.makeText(
                                    this@TrackingActivity,
                                    "Error Http status code $statusCode\n$content",
                                    Toast.LENGTH_LONG
                                ).show()
                                binding.tvToken.text = content
                                // Actualizar contenidos Tabs
                                observableData.setResultService("Error Http status code $statusCode\n$content")
                                //if (statusCode ==HttpURLConnection.HTTP_FORBIDDEN || statusCode ==HttpURLConnection.HTTP_UNAUTHORIZED ) {
                                //    // Eliminar login
                                //    LogoutUser();
                                //}
                            }
                        }

                        override fun onFailure(throwable: Throwable) {
                            /* An exception occurred during the request. Usually unable to connect or there was an error reading the response */
                            Log.e(TAG, "ERROR: " + throwable.message)
                            // RunOnUiThread  (Post se ejecuta en otro thread)
                            runOnUiThread {
                                Toast.makeText(
                                    this@TrackingActivity,
                                    "ERROR: " + throwable.message,
                                    Toast.LENGTH_LONG
                                ).show()
                                // Actualizar contenidos Tabs
                                binding.tvToken.text = throwable.message
                                observableData.setResultService("ERROR: " + throwable.message)
                            }
                        }
                    })
                }

                override fun onAccessTokenError(errorCode: Int, errorMessage: String) {
                    // Manejar error
                    Log.d(TAG, "ERROR: OAuth: $errorMessage")
                    // Actualizar contenidos Tabs
                    observableData.setAccesstoken("ERROR OAuth: $errorMessage")
                    OAuthErrorHandler(errorCode, errorMessage)
                    binding.tvToken.text = errorMessage
                }
            })
    }

    fun OAuthErrorHandler(errorCode: Int, errorMsg: String) {
        // Manejador error de OAuth
        //------------------------------------------
        if (errorCode == ErrorCode.UserCancel) {
            // El usuario canceló la pantalla de login.
            finish()
        } else if (errorCode == ErrorCode.ConnectionError) {
            Toast.makeText(
                this@TrackingActivity,
                "La conexión de red no está disponible.",
                Toast.LENGTH_LONG
            ).show()
        } else {
            Toast.makeText(
                this@TrackingActivity,
                "Se ha producido un error en la autenticación no siendo posible el acceso al recurso. \n$errorMsg",
                Toast.LENGTH_LONG
            ).show()
        }
    }


    fun LogoutUser() {
        // Logout
        OAuthManager.getInstance().Logout(this@TrackingActivity, object : LogoutListener {
            override fun onSuccess() {
                Log.d(TAG, "Logout success.")
                Toast.makeText(this@TrackingActivity, "Logout success", Toast.LENGTH_LONG).show()
                // Actualzar datos Tabs
                observableData.setAccesstoken("")
                observableData.setUserLogin("")
                observableData.setResultService("")
                binding.tvToken.text = "Sesión cerrada"
            }

            override fun onError(errorCode: Int, errorMessage: String) {
                Log.e(TAG, errorMessage)
                Toast.makeText(this@TrackingActivity, "Error Logout: $errorMessage", Toast.LENGTH_LONG)
                    .show()
            }
        })
    }

}