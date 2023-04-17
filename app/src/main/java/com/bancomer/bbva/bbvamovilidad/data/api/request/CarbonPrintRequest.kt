package com.bancomer.bbva.bbvamovilidad.data.api.request

data class CarbonPrintRequest(
    var codCentroTrabajoDestino: Int? = 0,
    var cpOrigen: String? = null,
    var detalle: List<Detalle>? = null,
    var usuarioM: String? = null
)