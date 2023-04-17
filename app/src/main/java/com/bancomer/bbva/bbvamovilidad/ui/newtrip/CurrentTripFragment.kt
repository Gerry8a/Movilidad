package com.bancomer.bbva.bbvamovilidad.ui.newtrip

import android.annotation.SuppressLint
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bancomer.bbva.bbvamovilidad.R
import com.bancomer.bbva.bbvamovilidad.data.api.ApiResponseStatus
import com.bancomer.bbva.bbvamovilidad.data.api.request.CarbonPrintRequest
import com.bancomer.bbva.bbvamovilidad.data.api.request.Detalle
import com.bancomer.bbva.bbvamovilidad.data.api.response.Medio
import com.bancomer.bbva.bbvamovilidad.databinding.FragmentCurrentTripBinding
import com.bancomer.bbva.bbvamovilidad.ui.base.BaseFragment
import com.bancomer.bbva.bbvamovilidad.utils.Dictionary.ADDRESS
import com.bancomer.bbva.bbvamovilidad.utils.Dictionary.DESTINATION
import com.bancomer.bbva.bbvamovilidad.utils.Dictionary.REQUEST
import com.bancomer.bbva.bbvamovilidad.utils.Dictionary.STRING_CLASS
import com.bancomer.bbva.bbvamovilidad.utils.Dictionary.STRING_DETAIL
import com.bancomer.bbva.bbvamovilidad.utils.Dictionary.TAG
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import kotlin.collections.ArrayList

@AndroidEntryPoint
class CurrentTripFragment : BaseFragment() {

    private lateinit var binding: FragmentCurrentTripBinding
    private val listMedio = ArrayList<Medio>()
    private val viewModel: DataViewModel by viewModels()
    private var requestCarbonPrint: String? = null
    private var detail: String? = null
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private var endingLatitude: Double = 0.0
    private var endingLongitude: Double = 0.0
    private var startTimestamp: Long = 0L

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCurrentTripBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()


        arguments?.let {
            requestCarbonPrint = it.getString(REQUEST)
            detail = it.getString(STRING_DETAIL)
            Log.d(TAG, "onViewCreated: $requestCarbonPrint")
        }

        binding.btnEndTrip.setOnClickListener {
            buildDetail()
        }
    }

    private fun getTimestamp(): Long {
        return System.currentTimeMillis()
    }

    private fun buildDetail() {
        val gson = Gson()
        val carbonPrint = gson.fromJson(requestCarbonPrint, CarbonPrintRequest::class.java)
        val detalle = gson.fromJson(detail, Detalle::class.java)

        getLatLng()

        detalle.paradaLatitud = endingLatitude
        detalle.paradaLongitud = endingLongitude
        detalle.kmRecorrido = calculateDistance(detalle)
        detalle.fhFinRecorrido = startTimestamp
        detalle.fhIniRecorrido = getTimestamp()

        val listDetalle = ArrayList<Detalle>()
        listDetalle.add(detalle)
        carbonPrint.detalle = listDetalle

        viewModel.sendRequest(carbonPrint)
        viewModel.status.observe(requireActivity()) {
            when (it) {
                is ApiResponseStatus.Error -> shortToast("Error")
                is ApiResponseStatus.Loading -> shortToast("Cargando")
                is ApiResponseStatus.Success -> shortToast("Viaje registrado")
            }
        }
    }

    private fun calculateDistance(detalle: Detalle): Float? {
        val result = FloatArray(10)
//        Location.distanceBetween(19.4238981, -99.173498, 19.4666222, -99.129734, result)
//        Log.d(TAG, "DISTANCIA: ${locationPruba.toString()}")
        Location.distanceBetween(detalle.origenLatitud!!,detalle.origenLongitud!!,detalle.paradaLatitud!!,detalle.paradaLongitud!!,result)

        val s = String.format("%.1f", result[0] / 1000)
        shortToast(s)
        Log.d(TAG, "buildDetail: ${s.toString()}")
        return result[0] / 1000
    }

    @SuppressLint("MissingPermission")
    private fun getLatLng() {
        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireActivity())
        fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                endingLatitude = location.latitude
                endingLongitude = location.longitude
            }
        }
    }

    private fun initView() {
        setUpToolbar()
        startTimestamp = getTimestamp()
        binding.tvEndTrip.text = getString(R.string.destination, preferences.get(DESTINATION, ""))
        binding.tvStartTrip.text =
            getString(R.string.current_location, preferences.get(ADDRESS, ""))

        val medio = getMedio(preferences.get(STRING_CLASS, "") as String)

        val manager = LinearLayoutManager(requireContext())
        listMedio.add(medio)
        binding.recyclerView.adapter = TransportAdapter(listMedio) {}
        binding.recyclerView.layoutManager = manager
        binding.recyclerView.setHasFixedSize(true)

    }

    private fun getMedio(stringClass: String): Medio {
        val gson = Gson()
        return gson.fromJson(stringClass, Medio::class.java)
    }

    private fun setUpToolbar() {
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
        binding.toolbar.toolbar.title = getString(R.string.current_trip)
    }

    override fun onDestroy() {
        super.onDestroy()
        (activity as AppCompatActivity?)!!.supportActionBar!!.show()
    }


}