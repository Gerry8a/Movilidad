package com.bancomer.bbva.bbvamovilidad.data.api.request

data class UserUpdateWorkCenter(
    val userM: String,
    val email: String,
    val codCentroTrabajo: Int
)