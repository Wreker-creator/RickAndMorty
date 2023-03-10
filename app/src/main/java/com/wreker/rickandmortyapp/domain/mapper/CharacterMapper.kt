package com.wreker.rickandmortyapp.domain.mapper

import com.wreker.rickandmortyapp.model.GetCharacterByIdResponse
import com.wreker.rickandmortyapp.domain.model.Character

object CharacterMapper {

    fun buildFrom(response : GetCharacterByIdResponse) : Character{

        return Character(
            episodeList = emptyList(),//todo
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