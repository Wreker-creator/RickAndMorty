package com.wreker.rickandmortyapp.model

data class GetCharactersPageResponse(
    val info : Info? = Info(),
    val results : List<GetCharacterByIdResponse> = emptyList()
)
