package com.bancomer.bbva.bbvamovilidad.data.api.response

data class Data(
    val centroTrabajoList: List<CentroTrabajo>,
    val gpoMedioList: List<GpoMedio>,
    val version: String
)