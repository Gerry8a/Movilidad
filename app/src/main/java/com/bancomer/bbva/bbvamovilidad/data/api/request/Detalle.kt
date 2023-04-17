package com.bancomer.bbva.bbvamovilidad.data.api.request

data class Detalle(
    var fhFinRecorrido: Long? = null,
    var fhIniRecorrido: Long? = null,
    var idMedioTraslado: Int? = null,
    var kmRecorrido: Float? = null,
    var origenLatitud: Double? = null,
    var origenLongitud: Double? = null,
    var paradaLatitud: Double? = null,
    var paradaLongitud: Double? = null
)