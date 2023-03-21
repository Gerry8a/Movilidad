package com.bancomer.bbva.bbvamovilidad.ui.home

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bancomer.bbva.bbvamovilidad.ObservableData
import com.bancomer.bbva.bbvamovilidad.R
import com.bancomer.bbva.bbvamovilidad.data.api.ApiResponseStatus
import com.bancomer.bbva.bbvamovilidad.data.api.ApiServiceInterceptor
import com.bancomer.bbva.bbvamovilidad.databinding.FragmentHomeBinding
import com.bancomer.bbva.bbvamovilidad.utils.Dictionary.TAG
import com.bbva.login.ErrorCode
import com.bbva.login.OAuthManager
import com.google.gson.GsonBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.concurrent.thread

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private var observableData = ObservableData()
    private val viewModel: CatalogViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        ApiServiceInterceptor.("72e2e780397bdc9eedd34c1c78c38ccd")
        buildObservers()
        initView()

//        viewModel.movie.observe(requireActivity()) {
//            binding.tvResult.text = it.title
//        }


//        thread {
//            val popularMovies =
//                MovieDBclient.service.listPopularMovies("72e2e780397bdc9eedd34c1c78c38ccd")
//            val body = popularMovies.execute().body()
//
//            if (body != null) {
//                binding.tvResult.text = body.results.get(0).title
//                Log.d("GGG", "onViewCreated: ${body.results.get(0).title}")
//            }
//        }
    }

    private fun initView() {

    }

    private fun buildObservers() {
        viewModel.status.observe(requireActivity()) { status ->
            when (status) {
                is com.bancomer.bbva.bbvamovilidad.data.api.ApiResponseStatus.Error -> {
                    Toast.makeText(
                        requireContext(),
                        "Error UI",
                        Toast.LENGTH_SHORT
                    ).show()

                    binding.pb.visibility = View.GONE
                }

                is com.bancomer.bbva.bbvamovilidad.data.api.ApiResponseStatus.Loading -> {

                    binding.pb.visibility = View.VISIBLE
                }
                is com.bancomer.bbva.bbvamovilidad.data.api.ApiResponseStatus.Success -> {
                    Toast.makeText(requireContext(), "SUCCESS", Toast.LENGTH_SHORT).show()
                    binding.pb.visibility = View.GONE
                }
            }
        }

    }

    override fun onResume() {
        super.onResume()
        observableData.setAccesstoken("")
        observableData.setUserLogin("")
        val context: Context = requireContext()
        OAuthManager.getInstance().requestAccessToken(context, object :
            OAuthManager.RequestAccessTokenListener {
            override fun onAccessTokenSuccess(token: String) {
                // Actualizar datos Tabs
                val gson = GsonBuilder().setPrettyPrinting().create() //new Gson();
                observableData.setAccesstoken(token)
                lifecycleScope.launch(Dispatchers.IO) {
//                    val accessToken = "Bearer $token"
//                    ApiServiceInterceptor.setSessionToken(token)
                    ApiServiceInterceptor.setSessionToken(token)
                    Log.d(TAG, "onAccessTokenSuccess: $token")
                    viewModel.downloadCatalog()

                }
                observableData.setUserLogin(
                    gson.toJson(
                        OAuthManager.getInstance().getUser(context)
                    )
                )
                ApiServiceInterceptor.setSessionToken(token)
              
                val user = gson.toJson(
                    OAuthManager.getInstance().getUser(context)
                )
                println("token: $token")
                println("user: $user")
            }

            override fun onAccessTokenError(errorCode: Int, errorMessage: String) {
                OAuthErrorHandler(errorCode, errorMessage)
                // Actualizar datos Tabs
                observableData.setAccesstoken("Error OAuth: $errorCode $errorMessage")
                observableData.setUserLogin("")
                Log.d(TAG, "onAccessTokenError: $errorMessage")
            }
        })

    }


    fun OAuthErrorHandler(errorCode: Int, errorMsg: String) {
        // Manejador error de OAuth
        //------------------------------------------
        if (errorCode == ErrorCode.UserCancel) {
            // El usuario canceló la pantalla de login.
            activity?.finish()
        } else if (errorCode == ErrorCode.ConnectionError) {
            Toast.makeText(
                requireContext(),
                "La conexión de red no está disponible.",
                Toast.LENGTH_LONG
            ).show()
        } else {
            Toast.makeText(
                requireContext(),
                "Se ha producido un error en la autenticación no siendo posible el acceso al recurso. \n$errorMsg",
                Toast.LENGTH_LONG
            ).show()
        }
    }


}