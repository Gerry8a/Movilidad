package com.bancomer.bbva.bbvamovilidad.data.api.response

data class CentroTrabajo(
    val centroTrabajo: String,
    val codCentroTrabajo: Int,
    val cp: String,
    val latitud: Double,
    val longitud: Double
)