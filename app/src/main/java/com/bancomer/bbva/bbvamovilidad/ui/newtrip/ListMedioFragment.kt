package com.bancomer.bbva.bbvamovilidad.ui.newtrip

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.bancomer.bbva.bbvamovilidad.R
import com.bancomer.bbva.bbvamovilidad.data.api.ApiResponseStatus
import com.bancomer.bbva.bbvamovilidad.data.api.response.Medio
import com.bancomer.bbva.bbvamovilidad.databinding.FragmentListMedioBinding
import com.bancomer.bbva.bbvamovilidad.ui.base.BaseFragment
import com.bancomer.bbva.bbvamovilidad.ui.home.CatalogViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ListMedioFragment : BaseFragment() {

    private lateinit var binding: FragmentListMedioBinding
    private val viewModel: CatalogViewModel by viewModels()
    private val listMedio = ArrayList<Medio>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentListMedioBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpToolBar()
        viewModel.downloadCatalog()
        viewModel.catalog.observe(requireActivity()){
            when(it){
                is ApiResponseStatus.Error -> shortToast("Errro")
                is ApiResponseStatus.Loading -> shortToast("Loading")
                is ApiResponseStatus.Success -> {
                    for (medio in it.data.gpoMedioList[2].medios){
                        fillData(medio)
                    }
                }
                else -> {}
            }
        }
    }

    private fun setUpToolBar() {
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
        binding.tbFragment.toolbar.title = getString(R.string.trasnportation_type)
        binding.tbFragment.toolbar
    }

    private fun fillData(medio: Medio) {
        val manager = LinearLayoutManager(requireContext())
        listMedio.add(medio)
        binding.rvListmedios.adapter = TransportAdapter(listMedio){medio -> onItemSelected(medio)}
        binding.rvListmedios.layoutManager = manager
        binding.rvListmedios.setHasFixedSize(true)
        val decoration = DividerItemDecoration(requireContext(), manager.orientation)
        binding.rvListmedios.addItemDecoration(decoration)
    }

    private fun onItemSelected(medio: Medio){
        longToast(medio.nomMedioTraslado)
    }

    override fun onDestroy() {
        super.onDestroy()
        (activity as AppCompatActivity?)!!.supportActionBar!!.show()
    }
}