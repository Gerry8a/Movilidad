package com.bancomer.bbva.bbvamovilidad.ui.newtrip

import android.content.ClipData.Item
import android.text.Layout
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bancomer.bbva.bbvamovilidad.R
import com.bancomer.bbva.bbvamovilidad.data.api.response.Medio
import com.bancomer.bbva.bbvamovilidad.databinding.ItemMedioBinding

class TransportAdapter(
    private val medioList: List<Medio>,
    private val onClickListener: (Medio) -> Unit
) : RecyclerView.Adapter<TransportAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemMedioBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       holder.bind(medioList[position], onClickListener)
    }

    override fun getItemCount(): Int = medioList.size

    class ViewHolder(private val binding: ItemMedioBinding) :
        RecyclerView.ViewHolder(binding.root) {

            fun bind(medio: Medio, onClickListener: (Medio) -> Unit){
                binding.tvNombreMedio.text = medio.nomMedioTraslado
                binding.tvGeneraKm.text = itemView.context.resources.getText(R.string.emission, medio.numEmisionCo2e.toString())
                itemView.setOnClickListener { onClickListener(medio) }
                when(medio.idSemaforo){
                    1 -> {
                        binding.tvNivelCo.text = "Generación de CO2e bajo"
                        binding.tvNivelCo.setTextColor(ContextCompat.getColor(itemView.context, R.color.low_level))
                    }
                    2 -> {
                        binding.tvNivelCo.text = "Generación de CO2e medio"
                        binding.tvNivelCo.setTextColor(ContextCompat.getColor(itemView.context, R.color.medium_level))
                    }
                    3 -> {
                        binding.tvNivelCo.text = "Generación de CO2e alto"
                        binding.tvNivelCo.setTextColor(ContextCompat.getColor(itemView.context, R.color.hight_level))
                    }
                }
            }
    }
}