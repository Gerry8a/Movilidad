package com.bancomer.bbva.bbvamovilidad.ui.newtrip

import android.annotation.SuppressLint
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bancomer.bbva.bbvamovilidad.R
import com.bancomer.bbva.bbvamovilidad.data.UIState
import com.bancomer.bbva.bbvamovilidad.data.api.ApiResponseStatus
import com.bancomer.bbva.bbvamovilidad.data.api.request.CarbonPrintRequest
import com.bancomer.bbva.bbvamovilidad.data.api.request.Detalle
import com.bancomer.bbva.bbvamovilidad.data.api.response.Medio
import com.bancomer.bbva.bbvamovilidad.data.local.entities.MedioEntity
import com.bancomer.bbva.bbvamovilidad.databinding.FragmentCurrentTripBinding
import com.bancomer.bbva.bbvamovilidad.ui.base.BaseFragment
import com.bancomer.bbva.bbvamovilidad.utils.Dictionary.ADDRESS
import com.bancomer.bbva.bbvamovilidad.utils.Dictionary.DESTINATION
import com.bancomer.bbva.bbvamovilidad.utils.Dictionary.REQUEST
import com.bancomer.bbva.bbvamovilidad.utils.Dictionary.STRING_DETAIL
import com.bancomer.bbva.bbvamovilidad.utils.Dictionary.TAG
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.*

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
    private var ggg: String? = ""
    private var isAdding = false
    private var gson = Gson()
    private var medioList: MutableList<MedioEntity>? = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCurrentTripBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            requestCarbonPrint = it.getString(REQUEST)
            detail = it.getString(STRING_DETAIL)
            Log.d(TAG, "onViewCreated: $requestCarbonPrint")
        }

        initView()
        isAdding = preferences.get("TRANSPORTE_AGREGADO", false) as Boolean

        binding.btnEndTrip.setOnClickListener {
           getLatLng()
        }

        binding.btnAddTransportation.setOnClickListener {
            goToAddFragment()

        }
    }

    private fun goToAddFragment() {
        val carbonPrint = gson.fromJson(requestCarbonPrint, CarbonPrintRequest::class.java)
        val detalle = gson.fromJson(detail, Detalle::class.java)

        detalle.paradaLatitud = endingLatitude
        detalle.paradaLongitud = endingLongitude
        detalle.kmRecorrido = calculateDistance(detalle)
        detalle.fhFinRecorrido = getTimestamp()
        detalle.fhIniRecorrido = startTimestamp

        Log.d(TAG, "FECHA INICIO: ${detalle.fhIniRecorrido}")
        Log.d(TAG, "FECHA FIN: ${detalle.fhFinRecorrido}")

        val listDetalle = ArrayList<Detalle>()
        listDetalle.add(detalle)
        carbonPrint.detalle = listDetalle

        val ttt = gson.toJson(carbonPrint, CarbonPrintRequest::class.java)
        val bundle = bundleOf("OOO" to ttt)

        view?.findNavController()
            ?.navigate(R.id.action_currentTripFragment_to_addTransportationFragment, bundle)
    }

    private fun buildJson(ttt: CarbonPrintRequest) {
        val carbonPrintRequest = CarbonPrintRequest()
        val gson = Gson()
        val carbonPrint = gson.toJson(ttt, CarbonPrintRequest::class.java)
        val bundle = bundleOf("OOO" to carbonPrint)
        view?.findNavController()
            ?.navigate(R.id.action_currentTripFragment_to_addTransportationFragment, bundle)
    }



    private fun getTimestamp(): Long {
        return System.currentTimeMillis()
    }

    private fun buildDetail() {
        if (isAdding){
            val requestString = preferences.get("NUEVO_DETALLE", "") as String
            val request = gson.fromJson(requestString, CarbonPrintRequest::class.java)
            val lastDetail = request.detalle?.last()

            request.detalle?.last()?.fhFinRecorrido = getTimestamp()
            request.detalle?.last()?.kmRecorrido = calculateDistance(lastDetail!!)
            request.detalle?.last()?.paradaLatitud = endingLatitude
            request.detalle?.last()?.paradaLongitud = endingLongitude

            sendRequest(request)
//            print(request.toString())


        } else {
            val carbonPrint = gson.fromJson(requestCarbonPrint, CarbonPrintRequest::class.java)
            val detalle = gson.fromJson(detail, Detalle::class.java)

            detalle.paradaLatitud = endingLatitude
            detalle.paradaLongitud = endingLongitude
            detalle.kmRecorrido = calculateDistance(detalle)
            detalle.fhFinRecorrido = getTimestamp()
            detalle.fhIniRecorrido = startTimestamp

            Log.d(TAG, "FECHA INICIO: ${detalle.fhIniRecorrido}")
            Log.d(TAG, "FECHA FIN: ${detalle.fhFinRecorrido}")

            val listDetalle = ArrayList<Detalle>()
            listDetalle.add(detalle)
            carbonPrint.detalle = listDetalle

            if (isAdding){
                buildJson(carbonPrint)
            } else {
                sendRequest(carbonPrint)
            }
        }





    }

    private fun sendRequest(request: CarbonPrintRequest) {
        viewModel.sendRequest(request)
        viewModel.status.observe(requireActivity()) {
            when (it) {
                is ApiResponseStatus.Error -> {
                    binding.loading.root.visibility = View.GONE
                    shortToast(it.messageID)
                }
                is ApiResponseStatus.Loading -> binding.loading.root.visibility = View.VISIBLE
                is ApiResponseStatus.Success -> {
                    binding.loading.root.visibility = View.GONE
                    shortToast("Viaje registrado")
                }
            }
        }
    }

    private fun calculateDistance(detalle: Detalle): Float? {
        val result = FloatArray(10)
//        Location.distanceBetween(19.4238981, -99.173498, 19.4666222, -99.129734, result)
//        Log.d(TAG, "DISTANCIA: ${locationPruba.toString()}")

        runBlocking {
            Location.distanceBetween(
                detalle.origenLatitud!!,
                detalle.origenLongitud!!,
                detalle.paradaLatitud!!,
                detalle.paradaLongitud!!,
                result
            )
        }


        val s = String.format("%.1f", result[0] / 1000)
        shortToast(s)
        Log.d(TAG, "buildDetail: ${s.toString()}")
        return 4.5f
//        return result[0] / 1000
    }

    @SuppressLint("MissingPermission")
    private fun getLatLng() {
        lifecycleScope.launch(Dispatchers.IO) {
            fusedLocationProviderClient =
                LocationServices.getFusedLocationProviderClient(requireActivity())
            fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    endingLatitude = location.latitude
                    endingLongitude = location.longitude
                    buildDetail()
                }
            }
        }
    }

    private fun initView() {
        setUpToolbar()
        startTimestamp = getTimestamp()
        binding.tvEndTrip.text = getString(R.string.destination, preferences.get(DESTINATION, ""))
        binding.tvStartTrip.text =
            getString(R.string.current_location, preferences.get(ADDRESS, ""))


        buildObservers()
    }

    private fun buildObservers() {
        viewModel.getMedios()
        viewModel.listMedio.observe(requireActivity()){
            when(it){
                is UIState.Error -> shortToast("No hay medios agregados")
                is UIState.Loading -> {}
                is UIState.Success -> {
                    medioList = it.data
                    if (!medioList.isNullOrEmpty()){
                        val manager = LinearLayoutManager(requireContext())
                        binding.recyclerView.adapter = TransportAdapterDB(medioList!!) {}
                        binding.recyclerView.layoutManager = manager
                        binding.recyclerView.setHasFixedSize(true)
                    }
                }
            }
        }
    }

    private fun getMedio(stringClass: String): Medio {
        val gson = Gson()
        val medio = gson.fromJson(stringClass, Medio::class.java)
//        viewModel.saveMedio(medio)
        return medio
    }

    private fun setUpToolbar() {
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
        binding.toolbar.toolbar.title = getString(R.string.current_trip)
    }

    override fun onDestroy() {
        super.onDestroy()
        (activity as AppCompatActivity?)!!.supportActionBar!!.show()
    }

    override fun onResume() {
        super.onResume()
        callServiceTest("param1", "param2")
    }


}