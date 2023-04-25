package com.bancomer.bbva.bbvamovilidad.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.bancomer.bbva.bbvamovilidad.utils.Dictionary.MEDIO_TABLE

@Entity(tableName = MEDIO_TABLE)
data class MedioEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var asset1x: String,
    var idSemaforo: Int,
    var nomMedioTraslado: String,
    var numEmisionCo2e: Double
)