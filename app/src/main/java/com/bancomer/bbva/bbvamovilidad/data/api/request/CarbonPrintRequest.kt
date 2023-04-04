package com.bancomer.bbva.bbvamovilidad.data.api.request

data class CarbonPrintRequest(
    val codCentroTrabajoDestino: Int,
    val cpOrigen: String,
    val detalle: List<Detalle>,
    val usuarioM: String
)