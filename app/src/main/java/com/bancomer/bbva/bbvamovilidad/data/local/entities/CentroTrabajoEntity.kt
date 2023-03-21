package com.bancomer.bbva.bbvamovilidad.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.bancomer.bbva.bbvamovilidad.utils.Dictionary.CAMPUS_TABLE

@Entity(tableName = CAMPUS_TABLE)
data class CentroTrabajoEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 1,
    @ColumnInfo(name = "codCentroTrabajo")
    var codCentroTrabajo: Int? = null,
    @ColumnInfo(name = "centroTrabajo")
    var centroTrabajo: String? = null,
    @ColumnInfo(name = "latitud")
    var latitud: Double? = null,
    @ColumnInfo(name = "longitud")
    var longitud: Double? = null,
    @ColumnInfo(name = "cp")
    var cp: String? = null
)
