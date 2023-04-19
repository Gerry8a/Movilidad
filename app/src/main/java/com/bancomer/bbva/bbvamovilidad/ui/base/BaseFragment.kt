package com.bancomer.bbva.bbvamovilidad.ui.base

import android.Manifest
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.bancomer.bbva.bbvamovilidad.Environment
import com.bancomer.bbva.bbvamovilidad.ObservableData
import com.bancomer.bbva.bbvamovilidad.utils.Dictionary.TAG
import com.bancomer.bbva.bbvamovilidad.utils.Preferences
import com.bbva.login.OAuthManager
import com.google.gson.Gson
import com.mb3364.http.AsyncHttpClient
import com.mb3364.http.HttpClient
import com.mb3364.http.RequestParams
import com.mb3364.http.StringHttpResponseHandler
import javax.inject.Inject

abstract class BaseFragment : Fragment() {

    @Inject
    protected lateinit var preferences: Preferences
    private var observableData = ObservableData()


    fun longToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }

    fun shortToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                shortToast("Permisos aceptados")
                //setUpMap()
            } else {
                /**
                 * Se explica porqué se tiene que aceptar los permisos
                 */


                Log.d("ggg", "Acepta, prro")
                //  showShortToast(getString(R.string.alert_permission_needed))
            }
        }

    fun requestLocationPermission() {
        when {
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED -> {
                //setUpMap()
            }
            shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION) -> {
                /**
                 * Descripción de porqué debe aceptar el permiso
                 */
                Log.d("ggg", "requestLocationPermission: da permiso, perro")
                showAlertdialog(
                    "Atención",
                    "Es neesario aceptar los permisos para usar la aplicación"
                )
            }
            else -> {
                requestPermissionLauncher.launch(
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            }
        }
    }

    fun showAlertdialog(title: String, message: String) {
        AlertDialog.Builder(requireContext())
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("Aceptar") { _, _ ->
                requestPermissionLauncher
            }
            .setNegativeButton("Cancelar") { _, _ ->
                shortToast("Es necesario aceptar los permisos para usar la aplicación")
            }
    }

    fun callServiceTest(param1: String?, param2: String?) {
        // Ejemplo llamada servicio (en este caso UserInfo)
        val params = RequestParams() // <- parametros de ejemplo
        params.put("param1", param1)
        params.put("param2", param2)
        val client: HttpClient = AsyncHttpClient()
        client.userAgent = "Android client example"
        client.setHeader("Content-Type", "application/json")
        // Refrescar token antes de cada invocación a un servicio
        OAuthManager.getInstance()
            .requestAccessToken(requireContext(), object :
                OAuthManager.RequestAccessTokenListener {
                override fun onAccessTokenSuccess(token: String) {
                    // Establecer access token
                    Log.d(TAG, "Header token: $token")
                    client.setHeader("Authorization", "Bearer $token") // <--- Token
                    // Actualizar datos Tabs
                    observableData.setAccesstoken(token)
                    // POST request  --> Debería ser GET pero GCP lo tienen mal publicado
                    client.post(
                        Environment.URL_SERVICE,
                        params,
                        object : StringHttpResponseHandler() {
                            override fun onSuccess(
                                statusCode: Int,
                                headers: Map<String, List<String>>,
                                result: String
                            ) {
                                Log.d(TAG, "Result: $result")
                                // RunOnUiThread  (Post se ejecuta en otro thread)
                                activity?.runOnUiThread { // Actualizar contenidos Tabs
                                    observableData.setResultService(result)
//                                binding.tvToken.text = result
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
                                activity?.runOnUiThread {
                                    Toast.makeText(
                                        requireContext(),
                                        "Error Http status code $statusCode\n$content",
                                        Toast.LENGTH_LONG
                                    ).show()
//                                binding.tvToken.text = content
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
                                activity?.runOnUiThread {
                                    Toast.makeText(
                                        requireContext(),
                                        "ERROR: " + throwable.message,
                                        Toast.LENGTH_LONG
                                    ).show()
                                    // Actualizar contenidos Tabs
//                                binding.tvToken.text = throwable.message
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
//                    OAuthErrorHandler(errorCode, errorMessage)
//                    binding.tvToken.text = errorMessage
                }
            })
    }
}