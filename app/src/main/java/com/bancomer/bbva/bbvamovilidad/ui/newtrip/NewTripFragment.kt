package com.bancomer.bbva.bbvamovilidad.ui.newtrip

import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
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
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewTripFragment : BaseFragment() {

    private lateinit var binding: FragmentNewTripBinding
    private val viewModel: UserViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNewTripBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val privacityAccepted = preferences.get(Dictionary.USER_ACCEPT_TERM, false) as Boolean

        // TODO: Realizar flujo cuando ya aceptó el aviso de privacidad
        if (!privacityAccepted) showNoticePrivacity()
        getAddress()

    }

    private fun getAddress() {

    }

    private fun buildObservers() {
        viewModel.userInfo.observe(requireActivity()) {
            when (it) {
                is UIState.Error -> TODO()
                is UIState.Loading -> {}
                is UIState.Success -> {
                    checkIfUserAccepted(it.data!!)
                }
            }
        }
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