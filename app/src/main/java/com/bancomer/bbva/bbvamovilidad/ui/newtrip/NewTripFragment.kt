package com.bancomer.bbva.bbvamovilidad.ui.newtrip

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Resources
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.bancomer.bbva.bbvamovilidad.R
import com.bancomer.bbva.bbvamovilidad.data.UIState
import com.bancomer.bbva.bbvamovilidad.data.api.ApiResponseStatus
import com.bancomer.bbva.bbvamovilidad.data.api.request.CarbonPrintRequest
import com.bancomer.bbva.bbvamovilidad.data.api.request.Detalle
import com.bancomer.bbva.bbvamovilidad.data.api.response.Medio
import com.bancomer.bbva.bbvamovilidad.data.local.entities.UserEntity
import com.bancomer.bbva.bbvamovilidad.databinding.FragmentNewTripBinding
import com.bancomer.bbva.bbvamovilidad.ui.base.BaseFragment
import com.bancomer.bbva.bbvamovilidad.ui.home.UserViewModel
import com.bancomer.bbva.bbvamovilidad.utils.BitmapUtils
import com.bancomer.bbva.bbvamovilidad.utils.Dictionary
import com.bancomer.bbva.bbvamovilidad.utils.Dictionary.ADDRESS
import com.bancomer.bbva.bbvamovilidad.utils.Dictionary.DESTINATION
import com.bancomer.bbva.bbvamovilidad.utils.Dictionary.ID_MEDIO
import com.bancomer.bbva.bbvamovilidad.utils.Dictionary.PRIVACY_NOTICE
import com.bancomer.bbva.bbvamovilidad.utils.Dictionary.REQUEST
import com.bancomer.bbva.bbvamovilidad.utils.Dictionary.STRING_CLASS
import com.bancomer.bbva.bbvamovilidad.utils.Dictionary.STRING_DETAIL
import com.bancomer.bbva.bbvamovilidad.utils.Dictionary.USERM
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException
import java.util.*

@AndroidEntryPoint
class NewTripFragment : BaseFragment() {

    private lateinit var binding: FragmentNewTripBinding
    private val viewModel: UserViewModel by viewModels()
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private var codCentroTrabajo: Int = 0
    private lateinit var cpOrigen: String
    private lateinit var detailString: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNewTripBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val privacyAccepted = preferences.get(Dictionary.USER_ACCEPT_TERM, false) as Boolean
        initView()

        if (!privacyAccepted) {
            showNoticePrivacy()
        } else {
            requestLocationPermission()
            getLatLng()
        }

        binding.cvtransportationtype.setOnClickListener {
            view.findNavController().navigate(R.id.action_newTripFragment_to_listMedioFragment)
        }

        binding.btnNewTrip.setOnClickListener {
            val request = getRequest()
            val bundle = bundleOf(REQUEST to request, STRING_DETAIL to detailString)
            view.findNavController()
                .navigate(R.id.action_newTripFragment_to_currentTripFragment, bundle)
        }
    }

    private fun getRequest(): String {
        val request = CarbonPrintRequest()
        request.codCentroTrabajoDestino = codCentroTrabajo
        request.cpOrigen = cpOrigen
        request.usuarioM = preferences.get(USERM, "") as String
        val gson = Gson()
        return gson.toJson(request)
    }

    // TODO: Implementar esta vista 
    private fun initView() {
        viewModel.getUserInfoFromDB()
        viewModel.userInfo.observe(requireActivity()) {
            fillData(it.data!!)
        }

        var idSelected = preferences.get(ID_MEDIO, 0) as Int
        print(idSelected.toString())

        if (idSelected >= 1) printMedio()
    }

    private fun printMedio() {
        viewModel.deleteMedios()
        val params = preferences.get(STRING_CLASS, "") as String
        val gson = Gson()
        val medio = gson.fromJson(params, Medio::class.java)
        binding.tvTransportationType.text = medio.nomMedioTraslado
        binding.ivIcon.setImageBitmap(BitmapUtils.stringToBitmap(medio.asset1x))
        viewModel.saveMedio(medio)
    }

    @SuppressLint("MissingPermission")
    private fun getLatLng() {
        var lastLocation: Location
        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireActivity())
        fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                val latitude = location.latitude
                val longitude = location.longitude

                getAddress(latitude, longitude)
            }
        }
    }

    private fun getAddress(lat: Double, lng: Double) {
        val mGeocoder = Geocoder(requireActivity(), Locale.getDefault())
        lifecycleScope.launch(Dispatchers.Main) {
            try {
                val addressList: List<Address> =
                    mGeocoder.getFromLocation(lat, lng, 1) as List<Address>

                if (addressList.isNotEmpty()) {
                    val address = addressList[0]
                    val sb = StringBuilder()
                    for (i in 0 until address.maxAddressLineIndex) {
                        sb.append(address.getAddressLine(i)).append("\n")
                    }

                    if (address.premises != null)
                        sb.append(address.premises).append(", ")

                    sb.append(address.thoroughfare).append(" ")
                    sb.append(address.subThoroughfare).append(", ")
                    sb.append(address.subLocality)

                    val addressString = sb.toString()

                    binding.etAddress.text = addressString
                    preferences.save(ADDRESS, addressString)

                    cpOrigen = address.postalCode

                    convertDetailToToString(lat, lng)


                }
            } catch (e: IOException) {
//            Toast.makeText(applicationContext,"Unable connect to Geocoder",Toast.LENGTH_LONG).show()
            }
        }

    }

    private fun convertDetailToToString(lat: Double, lng: Double) {
        var detalle = Detalle()
        detalle.origenLatitud = lat
        detalle.origenLongitud = lng
        detalle.idMedioTraslado = preferences.get(ID_MEDIO, 0) as Int
        val gson = Gson()
        detailString = gson.toJson(detalle)
    }

    private fun fillData(user: UserEntity) {
        binding.tvWorkCenter.text = user.centroTrabajoAct.toString()
        preferences.save(DESTINATION, user.centroTrabajoAct.toString())
        codCentroTrabajo = user.codCentroTrabajo!!
    }

    private fun checkIfUserAccepted(it: UserEntity) {
        if (it.fhAceptaTerminos != null) {
            showNoticePrivacy()
        }
    }

    private fun showNoticePrivacy() {
        var fullScreen: Boolean = true
        val dialg = BottomSheetDialog(requireContext())
        dialg.setOnShowListener {
            val btSheet: FrameLayout =
                dialg.findViewById(com.google.android.material.R.id.design_bottom_sheet)
                    ?: return@setOnShowListener
            val bottomSheetBehavior = BottomSheetBehavior.from(btSheet)
            if (fullScreen && btSheet.layoutParams != null) {
                showFullScreenBottomSheet(btSheet)
            }
            btSheet.setBackgroundResource(android.R.color.background_dark)
            expandBottomSheet(bottomSheetBehavior)
        }
        val vieww = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_bottomsheet, null)
        val btnCancelButton = vieww.findViewById<Button>(R.id.btnCancel)
        btnCancelButton.setOnClickListener { dialg.dismiss() }
        val btnCancelIcon = vieww.findViewById<ImageView>(R.id.ibCanel)
        val tvPrivacy = vieww.findViewById<TextView>(R.id.tvPrivacy)

        tvPrivacy.setOnClickListener {
            val openURL = Intent(Intent.ACTION_VIEW)
            openURL.data = Uri.parse(PRIVACY_NOTICE)
            startActivity(openURL)
        }
        btnCancelIcon.setOnClickListener {
//            view?.findNavController()?.navigate(R.id.action_newTripFragment_to_homeFragment)
//            view?.findNavController()?.previousBackStackEntry
            dialg.dismiss()
        }
        val btnAccept = vieww.findViewById<Button>(R.id.btnAccept)
        btnAccept.setOnClickListener {
            viewModel.updateUserAcceptTerm(preferences.get(USERM, "") as String)
            viewModel.status.observe(requireActivity()) {
                when (it) {
                    is ApiResponseStatus.Error -> shortToast("Error")
                    is ApiResponseStatus.Loading -> shortToast("Loading")
                    is ApiResponseStatus.Success -> {
                        preferences.save(Dictionary.USER_ACCEPT_TERM, true)
                        dialg.dismiss()
                        requestLocationPermission()
                        getLatLng()
                    }
                }
            }
        }

        dialg.setContentView(vieww)
        dialg.show()
    }


    private fun expandBottomSheet(bottomSheetBehavior: BottomSheetBehavior<FrameLayout>) {
        bottomSheetBehavior.skipCollapsed = true
        bottomSheetBehavior.isDraggable = false
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    private fun showFullScreenBottomSheet(bottomSheet: FrameLayout) {
        val layoutParams = bottomSheet.layoutParams
        layoutParams.height = Resources.getSystem().displayMetrics.heightPixels
        bottomSheet.layoutParams = layoutParams
    }

}