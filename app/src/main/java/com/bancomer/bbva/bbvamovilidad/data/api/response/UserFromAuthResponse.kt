package com.bancomer.bbva.bbvamovilidad.data.api.response

data class UserFromAuthResponse(
    val GoogleUserId: String,
    val personEmail: String,
    val personFamilyName: String,
    val personGivenName: String,
    val personName: String
)