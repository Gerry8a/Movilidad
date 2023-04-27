package com.bancomer.bbva.bbvamovilidad.ui.newtrip

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.bancomer.bbva.bbvamovilidad.R
import com.bancomer.bbva.bbvamovilidad.data.api.ApiResponseStatus
import com.bancomer.bbva.bbvamovilidad.data.api.response.Medio
import com.bancomer.bbva.bbvamovilidad.databinding.FragmentListMedioBinding
import com.bancomer.bbva.bbvamovilidad.ui.MainActivity
import com.bancomer.bbva.bbvamovilidad.ui.base.BaseFragment
import com.bancomer.bbva.bbvamovilidad.ui.home.CatalogViewModel
import com.bancomer.bbva.bbvamovilidad.utils.Dictionary.ID_MEDIO
import com.bancomer.bbva.bbvamovilidad.utils.Dictionary.MEDIO_SELECCIONADO
import com.bancomer.bbva.bbvamovilidad.utils.Dictionary.STRING_CLASS
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


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
        viewModel.catalog.observe(requireActivity()) {
            when (it) {
                is ApiResponseStatus.Error -> {
                    binding.loading.root.visibility = View.GONE
                    shortToast("Error")
                }
                is ApiResponseStatus.Loading -> binding.loading.root.visibility = View.VISIBLE
                is ApiResponseStatus.Success -> {
                    binding.loading.root.visibility = View.GONE
                    for (medio in it.data.gpoMedioList[0].medios) {
                        fillData(medio)
                    }
                }
            }
        }
    }

    // TODO: Ocultar bottomNavigation cuando aparece el fragmento
    private fun setUpToolBar() {
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
        binding.tbFragment.toolbar.title = getString(R.string.trasnportation_type)
    }

    private fun fillData(medio: Medio) {
        lifecycleScope.launch(Dispatchers.Main) {
            val manager = LinearLayoutManager(requireContext())
            listMedio.add(medio)
            binding.rvListmedios.adapter =
                TransportAdapter(listMedio) { medio -> onItemSelected(medio) }
            binding.rvListmedios.layoutManager = manager
            binding.rvListmedios.setHasFixedSize(true)
            val decoration = DividerItemDecoration(requireContext(), manager.orientation)
            binding.rvListmedios.addItemDecoration(decoration)
        }
    }

    private fun onItemSelected(medio: Medio) {
        preferences.save(ID_MEDIO, medio.id)
        val gson = Gson()
        val stringClass = gson.toJson(medio)
        val bundle = bundleOf(MEDIO_SELECCIONADO to stringClass)
        preferences.save(STRING_CLASS, stringClass)
        view?.findNavController()
            ?.navigate(R.id.action_listMedioFragment_to_newTripFragment, bundle)
    }

    override fun onDestroy() {
        super.onDestroy()
        (activity as AppCompatActivity?)!!.supportActionBar!!.show()
    }
}