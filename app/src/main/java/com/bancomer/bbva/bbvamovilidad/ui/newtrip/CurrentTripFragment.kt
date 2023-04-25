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
import com.bancomer.bbva.bbvamovilidad.utils.Dictionary.STRING_CLASS
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
            val gt: String? = it.getString("TTT")
            if (gt.isNullOrEmpty()) {
                Log.d(TAG, "onViewCreated: ES BLANCO")
            } else {
                addTransport(gt)
            }
            val medioAgregado = it.getString("ADDED")
            if (medioAgregado.isNullOrEmpty()){
                Log.d(TAG, "agregado?: Sin transporte")
            } else {
                llenarAdapter(medioAgregado)
            }
        }

        initView()

        binding.btnEndTrip.setOnClickListener {
            getLatLng()
        }

        binding.btnAddTransportation.setOnClickListener {
            isAdding = true
            getLatLng()

        }
    }

    private fun llenarAdapter(medioAgregado: String) {

        val gson = Gson()
        val request = gson.fromJson(medioAgregado, CarbonPrintRequest::class.java)

        val mediosss = request.detalle

//        for(detalles in mediosss!!){
//            listMedio.add()
//        }

//        val listamedios = getMedio(medioAgregado)
//        val manager = LinearLayoutManager(requireContext())
//        binding.recyclerView.adapter = TransportAdapter(listMedio) {}
//        binding.recyclerView.layoutManager = manager
//        binding.recyclerView.setHasFixedSize(true)
    }

    private fun buildJson(ttt: CarbonPrintRequest) {
        val carbonPrintRequest = CarbonPrintRequest()
        val gson = Gson()
        val carbonPrint = gson.toJson(ttt, CarbonPrintRequest::class.java)
        val bundle = bundleOf("OOO" to carbonPrint)
        view?.findNavController()
            ?.navigate(R.id.action_currentTripFragment_to_addTransportationFragment, bundle)

    }

    private fun addTransport(gt: String) {
        val ttt = getMedio(gt)

        listMedio.add(ttt)
        binding.recyclerView.setHasFixedSize(true)
        Log.d(TAG, "addTransport: ${listMedio.size}")
    }

    private fun getTimestamp(): Long {
        return System.currentTimeMillis()
    }

    private fun buildDetail() {
        val gson = Gson()
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
            viewModel.sendRequest(carbonPrint)
            viewModel.status.observe(requireActivity()) {
                when (it) {
                    is ApiResponseStatus.Error -> shortToast(it.messageID)
                    is ApiResponseStatus.Loading -> shortToast("Cargando")
                    is ApiResponseStatus.Success -> shortToast("Viaje registrado")
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

        val medio = getMedio(preferences.get(STRING_CLASS, "") as String)

        buildObservers()

//        val ggg = Medio(
//            descSemaforo = "Medio",
//            hexColor = "#C49735",
//            id = 1,
//            idSemaforo = 3,
//            nomMedioTraslado = "Auto Eléctrico",
//            numEmisionCo2e = 80.000,
//            asset1x = "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"
//        )

//        val ttt = Medio(
//            descSemaforo = "Medio",
//            hexColor = "#C49735",
//            id = 1,
//            idSemaforo = 3,
//            nomMedioTraslado = "Auto Eléctrico",
//            numEmisionCo2e = 80.000,
//            asset1x = "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"
//        )

        listMedio.add(medio)
//        listMedio.add(ggg)
//        listMedio.add(ttt)
//        val manager = LinearLayoutManager(requireContext())
//        binding.recyclerView.adapter = TransportAdapter(listMedio) {}
//        binding.recyclerView.layoutManager = manager
//        binding.recyclerView.setHasFixedSize(true)


        checkNewTransport()
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

    private fun checkNewTransport() {
        if (ggg?.isBlank()!!) {
            Log.d(TAG, "checkNewTransport: ${ggg.toString()}")
        } else {
            Log.d(TAG, "checkNewTransport: Está vacío")
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