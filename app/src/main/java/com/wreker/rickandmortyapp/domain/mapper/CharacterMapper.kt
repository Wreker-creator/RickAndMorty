package com.wreker.rickandmortyapp.domain.mapper

import com.wreker.rickandmortyapp.model.GetCharacterByIdResponse
import com.wreker.rickandmortyapp.domain.model.Character
import com.wreker.rickandmortyapp.model.GetEpisodeByIdResponse

object CharacterMapper {

    fun buildFrom(
        response : GetCharacterByIdResponse,
        episodes : List<GetEpisodeByIdResponse> = emptyList()
    ) : Character{

        return Character(
            episodeList = episodes.map {episode->
                EpisodeMapper.buildFrom(episode)
            },
            gender = response.gender,
            id = response.id,
            image = response.image,
            location = Character.Location(
                name = response.location!!.name,
                url = response.location.url
            ),
            name = response.name,
            origin = com.wreker.rickandmortyapp.domain.model.Character.Origin(
                name = response.origin!!.name,
                url = response.origin.url
            ),
            species = response.species,
            status = response.status
        )

    }

}