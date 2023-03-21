package com.bancomer.bbva.bbvamovilidad.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.bancomer.bbva.bbvamovilidad.R
import com.bancomer.bbva.bbvamovilidad.data.api.ApiResponseStatus
import com.bancomer.bbva.bbvamovilidad.databinding.FragmentComparativeBinding
import com.bancomer.bbva.bbvamovilidad.ui.base.BaseFragment
import com.bancomer.bbva.bbvamovilidad.ui.home.UserViewModel
import com.bancomer.bbva.bbvamovilidad.utils.Dictionary
import com.bancomer.bbva.bbvamovilidad.utils.Dictionary.TAG
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ComparativeFragment : BaseFragment() {

    private lateinit var binding: FragmentComparativeBinding
    private val viewmodel: UserViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentComparativeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnRestartOnboarding.setOnClickListener {
            preferences.save(Dictionary.ONBOARDING_FINISHED, true)
            activity?.finish()
        }
        
        viewmodel.getUserInfo("XMF0673")
        
        viewmodel.status.observe(requireActivity()){
            when(it){
                is ApiResponseStatus.Error -> Log.d(TAG, "onViewCreated: Error")
                is ApiResponseStatus.Loading -> Log.d(TAG, "onViewCreated: LOADING")
                
                is ApiResponseStatus.Success -> Log.d(TAG, "onViewCreated: SUCCESS")
            }
        }        
        
    }


}