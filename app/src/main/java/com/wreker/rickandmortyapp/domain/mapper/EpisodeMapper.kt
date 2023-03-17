package com.wreker.rickandmortyapp.domain.mapper

import com.wreker.rickandmortyapp.domain.model.Episode
import com.wreker.rickandmortyapp.model.GetCharacterByIdResponse
import com.wreker.rickandmortyapp.model.GetEpisodeByIdResponse

object EpisodeMapper {

    fun buildFrom(
        networkEpisode : GetEpisodeByIdResponse,
        networkCharacters : List<GetCharacterByIdResponse> = emptyList()
    ) : Episode{

        return Episode(
            id = networkEpisode.id,
            name = networkEpisode.name,
            airDate = networkEpisode.air_date,
            seasonNumber = networkEpisode.episode?.let { getSeasonFromEpisodeString(it) },
            episodeNumber = networkEpisode.episode?.let { getEpisodeFromSeasonString(it) },
            characterList = networkCharacters.map {
                CharacterMapper.buildFrom(it)
            })

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