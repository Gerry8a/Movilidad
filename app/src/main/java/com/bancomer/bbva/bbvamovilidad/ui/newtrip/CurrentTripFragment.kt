package com.bancomer.bbva.bbvamovilidad.ui.newtrip

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bancomer.bbva.bbvamovilidad.databinding.FragmentCurrentTripBinding
import com.bancomer.bbva.bbvamovilidad.utils.Dictionary.TAG
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CurrentTripFragment : Fragment() {

    private lateinit var binding: FragmentCurrentTripBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCurrentTripBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val requestCarbonPrint: String?
        arguments?.let {
            requestCarbonPrint = it.getString("request")
            Log.d(TAG, "onViewCreated: $requestCarbonPrint")
        }
    }


}