package com.bancomer.bbva.bbvamovilidad.ui.newtrip

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.bancomer.bbva.bbvamovilidad.R
import com.bancomer.bbva.bbvamovilidad.data.api.ApiResponseStatus
import com.bancomer.bbva.bbvamovilidad.data.api.response.Medio
import com.bancomer.bbva.bbvamovilidad.databinding.FragmentAddtransportationBinding
import com.bancomer.bbva.bbvamovilidad.ui.base.BaseFragment
import com.bancomer.bbva.bbvamovilidad.ui.home.CatalogViewModel
import com.bancomer.bbva.bbvamovilidad.utils.Dictionary
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.internal.notify

@AndroidEntryPoint
class AddTransportationFragment : BaseFragment() {

    private lateinit var binding: FragmentAddtransportationBinding
    private val viewModel: CatalogViewModel by viewModels()
    private val listMedio = ArrayList<Medio>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddtransportationBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.downloadCatalog()
        viewModel.catalog.observe(requireActivity()){
            when(it){
                is ApiResponseStatus.Error -> shortToast("Error")
                is ApiResponseStatus.Loading -> {}
                is ApiResponseStatus.Success -> {
                    for (medio in it.data.gpoMedioList[0].medios){
                        fillData(medio)
                    }
                }
            }
        }
    }

    private fun fillData(medio: Medio) {
        lifecycleScope.launch(Dispatchers.Main){
            val manager = LinearLayoutManager(requireContext())
            listMedio.add(medio)
            binding.rvAddListMedio.adapter =
                TransportAdapter(listMedio) { medio -> onItemSelected(medio) }
            binding.rvAddListMedio.layoutManager = manager
            binding.rvAddListMedio.setHasFixedSize(true)
            val decoration = DividerItemDecoration(requireContext(), manager.orientation)
            binding.rvAddListMedio.addItemDecoration(decoration)
        }
    }

    private fun onItemSelected(medio: Medio) {
//        preferences.save(Dictionary.ID_MEDIO, medio.id)
        val gson = Gson()
        val stringClass = gson.toJson(medio)
        val bundle = bundleOf("TTT" to stringClass)
//        preferences.save(Dictionary.STRING_CLASS, stringClass)
        view?.findNavController()
            ?.navigate(R.id.action_addTransportationFragment_to_currentTripFragment, bundle)
    }

}