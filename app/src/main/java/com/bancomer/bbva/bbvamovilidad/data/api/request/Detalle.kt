package com.bancomer.bbva.bbvamovilidad.data.api.request

data class Detalle(
    var fhFinRecorrido: Long? = 0,
    var fhIniRecorrido: Long? = 0,
    var idMedioTraslado: Int = 0,
    var kmRecorrido: Float? = 0.0f,
    var origenLatitud: Double? = 0.0,
    var origenLongitud: Double? = 0.0,
    var paradaLatitud: Double? = 0.0,
    var paradaLongitud: Double? = 0.0
)