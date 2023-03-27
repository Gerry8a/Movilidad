package com.bancomer.bbva.bbvamovilidad.ui.onboarding

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bancomer.bbva.bbvamovilidad.R
import com.bancomer.bbva.bbvamovilidad.databinding.FragmentFourthOnboardingBinding


class FourthOnboardingFragment : Fragment() {

    private lateinit var binding: FragmentFourthOnboardingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFourthOnboardingBinding.inflate(layoutInflater, container, false)
        return binding.root
    }


}