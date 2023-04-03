package com.bancomer.bbva.bbvamovilidad.ui.newtrip

import android.annotation.SuppressLint
import android.content.res.Resources
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.bancomer.bbva.bbvamovilidad.R
import com.bancomer.bbva.bbvamovilidad.data.UIState
import com.bancomer.bbva.bbvamovilidad.data.api.ApiResponseStatus
import com.bancomer.bbva.bbvamovilidad.data.local.entities.UserEntity
import com.bancomer.bbva.bbvamovilidad.databinding.FragmentNewTripBinding
import com.bancomer.bbva.bbvamovilidad.ui.base.BaseFragment
import com.bancomer.bbva.bbvamovilidad.ui.home.UserViewModel
import com.bancomer.bbva.bbvamovilidad.utils.Dictionary
import com.bancomer.bbva.bbvamovilidad.utils.Dictionary.USERM
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
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

        if (!privacyAccepted) {
            showNoticePrivacity()
        } else {
            requestLocationPermission()
            getLatLng()
        }

//        buildObservers()
//        initView()
        binding.cvtransportationtype.setOnClickListener {
            view?.findNavController()?.navigate(R.id.action_newTripFragment_to_listMedioFragment)
        }
    }

    // TODO: Implementar esta vista 
    private fun initView() {
        viewModel.userInfo.observe(requireActivity()){
            fillData(it.data!!)
        }
    }

    @SuppressLint("MissingPermission")
    private fun getLatLng() {
        var lastLocation: Location
        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireActivity())
        fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                val currentLatLng = LatLng(location.latitude, location.longitude)
                getAddress(currentLatLng)
            }
        }
    }

    private fun getAddress(currentLatLng: LatLng) {
        val mGeocoder = Geocoder(requireActivity(), Locale.getDefault())
        val lat = currentLatLng.latitude
        val lng = currentLatLng.longitude
        lifecycleScope.launch(Dispatchers.Main) {
            try {
                val addressList: List<Address> =
                    mGeocoder.getFromLocation(lat, lng, 1) as List<Address>

                if (addressList != null && addressList.isNotEmpty()) {
                    val address = addressList[0]
                    val sb = StringBuilder()
                    for (i in 0 until address.maxAddressLineIndex) {
                        sb.append(address.getAddressLine(i)).append("\n")
                    }

                    if (address.premises != null)
                        sb.append(address.premises).append(", ")

                    sb.append(address.thoroughfare).append(" ")
                    sb.append(address.subThoroughfare)

                    val addressString = sb.toString()

                    binding.etAddress.text = addressString


                }
            } catch (e: IOException) {
//            Toast.makeText(applicationContext,"Unable connect to Geocoder",Toast.LENGTH_LONG).show()
            }
        }

    }

    private fun buildObservers() {
        viewModel.userInfo.observe(requireActivity()) {
            when (it) {
                is UIState.Error -> TODO()
                is UIState.Loading -> {}
                is UIState.Success -> {
//                    checkIfUserAccepted(it.data!!)
                    fillData(it.data!!)
                }
            }
        }
    }

    private fun fillData(user: UserEntity) {
        binding.tvWorkCenter.text = user.centroTrabajoAct.toString()
    }

    private fun checkIfUserAccepted(it: UserEntity) {
        if (it.fhAceptaTerminos != null) {
            showNoticePrivacity()
        }
    }

    private fun showNoticePrivacity() {
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