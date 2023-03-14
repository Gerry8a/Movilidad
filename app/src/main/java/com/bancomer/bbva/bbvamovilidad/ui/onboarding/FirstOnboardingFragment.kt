package com.bancomer.bbva.bbvamovilidad.ui.onboarding

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.bancomer.bbva.bbvamovilidad.R
import com.bancomer.bbva.bbvamovilidad.databinding.FragmentFistOnboardingFragemntBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class FirstOnboardingFragment : Fragment() {

    private lateinit var binding: FragmentFistOnboardingFragemntBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFistOnboardingFragemntBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

//    private fun initView() {
//        val places = resources.getStringArray(R.array.places)
//        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, places)
//        binding.autoCompleteTextView.setAdapter(arrayAdapter)
//    }

}