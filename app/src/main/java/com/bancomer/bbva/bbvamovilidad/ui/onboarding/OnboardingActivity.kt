package com.bancomer.bbva.bbvamovilidad.ui.onboarding

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import com.bancomer.bbva.bbvamovilidad.ObservableData
import com.bancomer.bbva.bbvamovilidad.data.api.ApiServiceInterceptor
import com.bancomer.bbva.bbvamovilidad.data.api.response.UserFromAuthResponse
import com.bancomer.bbva.bbvamovilidad.databinding.ActivityOnboardingBinding
import com.bancomer.bbva.bbvamovilidad.ui.base.BaseActivity
import com.bancomer.bbva.bbvamovilidad.ui.home.UserViewModel
import com.bancomer.bbva.bbvamovilidad.utils.Dictionary
import com.bancomer.bbva.bbvamovilidad.utils.Dictionary.TAG
import com.bancomer.bbva.bbvamovilidad.utils.Dictionary.ACCESS_TOKEN
import com.bancomer.bbva.bbvamovilidad.utils.Dictionary.USER_EMAIL
import com.bbva.login.EnvironmentType
import com.bbva.login.ErrorCode
import com.bbva.login.OAuthManager
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class OnboardingActivity : BaseActivity() {

    private lateinit var binding: ActivityOnboardingBinding
    private lateinit var navController: NavController
    private var observableData = ObservableData()
    private val viewModel: UserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpNavigation()
        login()

    }

    private fun login() {
        OAuthManager.getInstance().Configure(EnvironmentType.DEV)
    }

    private fun setUpNavigation() {
        supportActionBar?.hide()
    }


    override fun onResume() {
        super.onResume()

        // OAuth Initial Check
        //------------------------------------------
        // Login al iniciar aplicación
        observableData.setAccesstoken("")
        observableData.setUserLogin("")
        lifecycleScope.launch(Dispatchers.IO) {
            val context: Context = this@OnboardingActivity
            OAuthManager.getInstance().requestAccessToken(context, object :
                OAuthManager.RequestAccessTokenListener {
                override fun onAccessTokenSuccess(token: String) {
                    // Actualizar datos Tabs
                    val gson = GsonBuilder().setPrettyPrinting().create() //new Gson();
                    observableData.setAccesstoken(token)
                    observableData.setUserLogin(
                        gson.toJson(
                            OAuthManager.getInstance().getUser(context)
                        )
                    )
                    preferences.save(ACCESS_TOKEN, token)
                    Log.d(TAG, "onAccessTokenSuccess: $token")
                    val user = gson.toJson(
                        OAuthManager.getInstance().getUser(context)
                    )
                    println("token: $token")
                    ApiServiceInterceptor.setSessionToken(token)
                    saveUserFromAuth(user)
                    println("user: $user")
//                binding.tvUser.text = gson.toJson(
//                    OAuthManager.getInstance().getUser(context)
//                )
                }

                override fun onAccessTokenError(errorCode: Int, errorMessage: String) {
                    OAuthErrorHandler(errorCode, errorMessage)
                    // Actualizar datos Tabs
                    observableData.setAccesstoken("Error OAuth: $errorCode $errorMessage")
                    observableData.setUserLogin("")
//                binding.tvToken.text = errorMessage
                }
            })
        }

    }

    private fun saveUserFromAuth(user: String?) {
        val gson = Gson()
        val user = gson.fromJson(user, UserFromAuthResponse::class.java)
//        preferences.save(USER_EMAIL, user.personEmail)
//        val ggg = preferences.get(USER_EMAIL, "ggg").toString()
        viewModel.insertUser(user.personEmail)


    }

    fun OAuthErrorHandler(errorCode: Int, errorMsg: String) {
        // Manejador error de OAuth
        //------------------------------------------
        if (errorCode == ErrorCode.UserCancel) {
            // El usuario canceló la pantalla de login.
            finish()
        } else if (errorCode == ErrorCode.ConnectionError) {
            Toast.makeText(
                this@OnboardingActivity,
                "La conexión de red no está disponible.",
                Toast.LENGTH_LONG
            ).show()
        } else {
            Toast.makeText(
                this@OnboardingActivity,
                "Se ha producido un error en la autenticación no siendo posible el acceso al recurso. \n$errorMsg",
                Toast.LENGTH_LONG
            ).show()
        }
    }

}