package com.wreker.rickandmortyapp.domain.mapper

import com.wreker.rickandmortyapp.domain.model.Episode
import com.wreker.rickandmortyapp.model.GetEpisodeByIdResponse

object EpisodeMapper {

    fun buildFrom(episodeResponse : GetEpisodeByIdResponse) : Episode{

        return Episode(
            id = episodeResponse.id,
            name = episodeResponse.name,
            airDate = episodeResponse.air_date,
            episode = episodeResponse.episode
        )

    }

}