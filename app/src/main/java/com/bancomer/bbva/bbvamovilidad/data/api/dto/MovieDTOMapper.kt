package com.bancomer.bbva.bbvamovilidad.data.api.dto

import com.bancomer.bbva.bbvamovilidad.model.Movie

class MovieDTOMapper {
    private fun fromMovieDTOtoMovieDomain(m: MovieDTO): Movie {
        return Movie(
            m.adult,
            m.backdrop_path,
            m.genre_ids,
            m.id,
            m.original_language,
            m.original_title,
            m.overview,
            m.popularity,
            m.poster_path,
            m.release_date,
            m.title,
            m.video,
            m.vote_average,
            m.vote_count,
        )
    }

    fun fromMovieDTOListToMovieDomainList(mList: List<MovieDTO>): List<Movie>{
        return mList.map{fromMovieDTOtoMovieDomain(it)}

    }
}