package com.bancomer.bbva.bbvamovilidad.ui.newtrip

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bancomer.bbva.bbvamovilidad.R
import com.bancomer.bbva.bbvamovilidad.databinding.FragmentCurrentTripBinding
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

    }


}