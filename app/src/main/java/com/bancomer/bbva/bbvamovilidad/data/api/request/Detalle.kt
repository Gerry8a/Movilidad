package com.bancomer.bbva.bbvamovilidad.data.api.request

data class Detalle(
    val fhFinRecorrido: String,
    val fhIniRecorrido: String,
    val idMedioTraslado: Int,
    val kmRecorrido: Int,
    val origenLatitud: Double,
    val origenLongitud: Double,
    val paradaLatitud: Double,
    val paradaLongitud: Double
)