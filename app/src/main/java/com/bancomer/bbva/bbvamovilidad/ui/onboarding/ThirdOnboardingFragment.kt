package com.bancomer.bbva.bbvamovilidad.ui.onboarding

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.viewModels
import com.bancomer.bbva.bbvamovilidad.R
import com.bancomer.bbva.bbvamovilidad.data.api.ApiResponseStatus
import com.bancomer.bbva.bbvamovilidad.databinding.FragmentThirdOnboardingBinding
import com.bancomer.bbva.bbvamovilidad.ui.MainActivity
import com.bancomer.bbva.bbvamovilidad.ui.base.BaseFragment
import com.bancomer.bbva.bbvamovilidad.ui.home.UserViewModel
import com.bancomer.bbva.bbvamovilidad.utils.Dictionary.ONBOARDING_FINISHED
import com.bancomer.bbva.bbvamovilidad.utils.Dictionary.TRY_AGAIN
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ThirdOnboardingFragment : BaseFragment(), AdapterView.OnItemSelectedListener,
    AdapterView.OnItemClickListener {

    private lateinit var binding: FragmentThirdOnboardingBinding
    private val viewModel: UserViewModel by viewModels()
    private lateinit var workCenter: String

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
        buildObservers()

        binding.button.setOnClickListener {
            callServiceTest("", "")
            viewModel.getWorkCode(workCenter)
        }
    }

    private fun buildObservers() {
        viewModel.status.observe(requireActivity()){
            when(it){
                is ApiResponseStatus.Error -> {
                    binding.loading.root.visibility = View.GONE
                    shortToast(TRY_AGAIN)
                }
                is ApiResponseStatus.Loading -> binding.loading.root.visibility = View.VISIBLE
                is ApiResponseStatus.Success -> {
                    binding.loading.root.visibility = View.GONE
                    preferences.save(ONBOARDING_FINISHED, true)
                    val intent = Intent(requireContext(), MainActivity::class.java)
                    startActivity(intent)
                    activity?.finish()
                }
            }
        }
    }

    private fun initView() {
        val places = resources.getStringArray(R.array.places)
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, places)
        workCenter = binding.autoCompleteTextView.text.toString()
        with(binding.autoCompleteTextView){
            setAdapter(arrayAdapter)
            onItemClickListener = this@ThirdOnboardingFragment
        }
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        workCenter = parent?.getItemAtPosition(position).toString()
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        workCenter = parent?.getItemAtPosition(position).toString()
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {}


}