package com.bancomer.bbva.bbvamovilidad.data.api.response

data class Data(
    val centroTrabajoList: List<CentroTrabajo>,
    val medioTrasladoList: List<MedioTraslado>,
    val version: String
)