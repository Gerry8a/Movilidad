package com.bancomer.bbva.bbvamovilidad.data.api.response

data class GpoMedio(
    val id: Int,
    val medios: List<Medio>,
    val nomGpoMedioTraslado: String
)