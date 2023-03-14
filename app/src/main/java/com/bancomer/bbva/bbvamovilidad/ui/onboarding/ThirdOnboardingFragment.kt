package com.bancomer.bbva.bbvamovilidad.ui.onboarding

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.bancomer.bbva.bbvamovilidad.R
import com.bancomer.bbva.bbvamovilidad.databinding.FragmentThirdOnboardingBinding
import com.bancomer.bbva.bbvamovilidad.ui.MainActivity
import com.bancomer.bbva.bbvamovilidad.ui.base.BaseFragment
import com.bancomer.bbva.bbvamovilidad.utils.Dictionary.ONBOARDING_FINISHED
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ThirdOnboardingFragment : BaseFragment() {

    private lateinit var binding: FragmentThirdOnboardingBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentThirdOnboardingBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()

        val intent = Intent(requireContext(), MainActivity::class.java)
        binding.button.setOnClickListener {
            preferences.save(ONBOARDING_FINISHED, true)
            startActivity(intent)
            activity?.finish()
        }
    }

    private fun initView() {
        val places = resources.getStringArray(R.array.places)
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, places)
        binding.autoCompleteTextView.setAdapter(arrayAdapter)
    }
}