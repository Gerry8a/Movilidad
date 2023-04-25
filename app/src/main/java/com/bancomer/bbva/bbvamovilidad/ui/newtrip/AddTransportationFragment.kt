package com.bancomer.bbva.bbvamovilidad.ui.newtrip

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.bancomer.bbva.bbvamovilidad.R
import com.bancomer.bbva.bbvamovilidad.data.api.ApiResponseStatus
import com.bancomer.bbva.bbvamovilidad.data.api.request.CarbonPrintRequest
import com.bancomer.bbva.bbvamovilidad.data.api.request.Detalle
import com.bancomer.bbva.bbvamovilidad.data.api.response.Medio
import com.bancomer.bbva.bbvamovilidad.data.local.entities.MedioEntity
import com.bancomer.bbva.bbvamovilidad.databinding.FragmentAddtransportationBinding
import com.bancomer.bbva.bbvamovilidad.ui.base.BaseFragment
import com.bancomer.bbva.bbvamovilidad.ui.home.CatalogViewModel
import com.bancomer.bbva.bbvamovilidad.utils.Dictionary
import com.bancomer.bbva.bbvamovilidad.utils.Dictionary.TAG
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.internal.notify

@AndroidEntryPoint
class AddTransportationFragment : BaseFragment() {

    private lateinit var binding: FragmentAddtransportationBinding
    private val viewModel: CatalogViewModel by viewModels()
    private val listMedio = ArrayList<Medio>()
    private var requestString: String? = ""
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private var endingLatitude: Double = 0.0
    private var endingLongitude: Double = 0.0


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddtransportationBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            requestString = it.getString("OOO")
        }

        viewModel.downloadCatalog()
        viewModel.catalog.observe(requireActivity()){
            when(it){
                is ApiResponseStatus.Error -> shortToast("Error")
                is ApiResponseStatus.Loading -> {}
                is ApiResponseStatus.Success -> {
                    for (medio in it.data.gpoMedioList[0].medios){
                        fillData(medio)
                    }
                }
            }
        }
    }

    private fun fillData(medio: Medio) {
        lifecycleScope.launch(Dispatchers.Main){
            val manager = LinearLayoutManager(requireContext())
            listMedio.add(medio)
            binding.rvAddListMedio.adapter =
                TransportAdapter(listMedio) { medio -> onItemSelected(medio) }
            binding.rvAddListMedio.layoutManager = manager
            binding.rvAddListMedio.setHasFixedSize(true)
            val decoration = DividerItemDecoration(requireContext(), manager.orientation)
            binding.rvAddListMedio.addItemDecoration(decoration)
        }
    }

    private fun onItemSelected(medio: Medio) {
        getLatLng(medio)
//        preferences.save(Dictionary.ID_MEDIO, medio.id)
//        val gson = Gson()
//        val stringClass = gson.toJson(medio)
//        val bundle = bundleOf("TTT" to stringClass)
//        preferences.save(Dictionary.STRING_CLASS, stringClass)

    }

    @SuppressLint("MissingPermission")
    private fun getLatLng(medio: Medio) {
        lifecycleScope.launch(Dispatchers.IO) {
            fusedLocationProviderClient =
                LocationServices.getFusedLocationProviderClient(requireActivity())
            fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    endingLatitude = location.latitude
                    endingLongitude = location.longitude
                    buildDetail(medio)
                }
            }
        }
    }

    private fun buildDetail(medio: Medio) {
        val gson = Gson()
        val carbonPrintRequest = gson.fromJson(requestString, CarbonPrintRequest::class.java)

        viewModel.insertMedio(medio)

        val detalle = Detalle()
        detalle.origenLatitud = endingLatitude
        detalle.origenLongitud = endingLongitude
        detalle.idMedioTraslado = medio.id
        detalle.fhIniRecorrido = getTimestamp()

        val listDetalle = ArrayList<Detalle>()
        var listaPrueba: List<Detalle>? = carbonPrintRequest.detalle

        for (tt in listaPrueba!!){
            listDetalle.add(tt)
        }

        listDetalle.add(detalle)

        carbonPrintRequest.detalle = listDetalle

        val medioToString = gson.toJson(carbonPrintRequest)
        val bundle = bundleOf("ADDED" to medioToString)
        Log.d(TAG, "NUEVO MEDIO: $medioToString")
        view?.findNavController()
            ?.navigate(R.id.action_addTransportationFragment_to_currentTripFragment, bundle)
    }

    private fun getTimestamp(): Long? {
        return System.currentTimeMillis()
    }


}