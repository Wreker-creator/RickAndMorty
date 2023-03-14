package com.wreker.rickandmortyapp.model

data class GetEpisodeByPageResponse(
    val info: Info?,
    val results: List<GetEpisodeByIdResponse> = emptyList()
)