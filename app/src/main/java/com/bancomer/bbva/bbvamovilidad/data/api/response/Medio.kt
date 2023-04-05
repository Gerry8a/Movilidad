package com.bancomer.bbva.bbvamovilidad.data.api.response

data class Medio(
    val descSemaforo: String,
    val hexColor: String,
    val id: Int,
    val idSemaforo: Int,
    val nomMedioTraslado: String,
    val numEmisionCo2e: Double,
    val asset1x: String
)