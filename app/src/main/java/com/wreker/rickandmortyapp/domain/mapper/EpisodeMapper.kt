package com.wreker.rickandmortyapp.domain.mapper

import com.wreker.rickandmortyapp.domain.model.Episode
import com.wreker.rickandmortyapp.model.GetEpisodeByIdResponse

object EpisodeMapper {

    fun buildFrom(episodeResponse : GetEpisodeByIdResponse) : Episode{

        return Episode(
            id = episodeResponse.id,
            name = episodeResponse.name,
            airDate = episodeResponse.air_date,
            seasonNumber = episodeResponse.episode?.let { getSeasonFromEpisodeString(it) },
            episodeNumber = episodeResponse.episode?.let { getEpisodeFromSeasonString(it) }
        )

    }

    //we are accepting response for season number and episode number in the form of int
    //both of which are present in the field episode, so we handle that here

    private fun getSeasonFromEpisodeString(episode : String) : Int{
        val endIndex = episode.indexOfFirst {
            it.equals('e', true)
        }

        if(endIndex == -1){
            return 0
        }

        return episode.substring(1, endIndex).toIntOrNull() ?: 0
    }

    private fun getEpisodeFromSeasonString(episode : String) : Int{
        val startIndex = episode.indexOfFirst {
            it.equals('e', true)
        }

        if(startIndex == -1){
            return 0
        }

        return episode.substring(startIndex+1).toIntOrNull() ?: 0
    }

}