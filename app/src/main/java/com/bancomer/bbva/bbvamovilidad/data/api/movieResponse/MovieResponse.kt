package com.bancomer.bbva.bbvamovilidad.data.api.movieResponse

import com.bancomer.bbva.bbvamovilidad.data.api.dto.MovieDTO


data class MovieResponse(
    val page: Int,
    val results: List<MovieDTO>,
    val total_pages: Int,
    val total_results: Int
)