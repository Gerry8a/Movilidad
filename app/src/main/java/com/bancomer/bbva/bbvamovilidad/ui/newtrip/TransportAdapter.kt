package com.bancomer.bbva.bbvamovilidad.ui.newtrip

import android.content.ClipData.Item
import android.text.Layout
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bancomer.bbva.bbvamovilidad.R
import com.bancomer.bbva.bbvamovilidad.data.api.response.Medio
import com.bancomer.bbva.bbvamovilidad.data.local.entities.MedioEntity
import com.bancomer.bbva.bbvamovilidad.databinding.ItemMedioBinding
import com.bancomer.bbva.bbvamovilidad.utils.BitmapUtils
import com.bancomer.bbva.bbvamovilidad.utils.Dictionary.TAG
import com.bumptech.glide.Glide
import okio.blackholeSink

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
                binding.tvGeneraKm.text = itemView.context.resources.getString(R.string.emission, medio.numEmisionCo2e.toString())
                itemView.setOnClickListener { onClickListener(medio) }
                when(medio.idSemaforo){
                    1 -> {
                        binding.tvNivelCo.text = itemView.context.getString(R.string.generate_low)
                        binding.tvNivelCo.setTextColor(ContextCompat.getColor(itemView.context, R.color.low_level))
                    }
                    2 -> {
                        binding.tvNivelCo.text = itemView.context.getString(R.string.generate_medium)
                        binding.tvNivelCo.setTextColor(ContextCompat.getColor(itemView.context, R.color.medium_level))
                    }
                    3 -> {
                        binding.tvNivelCo.text = itemView.context.getString(R.string.generate_high)
                        binding.tvNivelCo.setTextColor(ContextCompat.getColor(itemView.context, R.color.hight_level))
                    }
                }
                binding.ivIconMedio.setImageBitmap(BitmapUtils.stringToBitmap(medio.asset1x))
            }
    }
}