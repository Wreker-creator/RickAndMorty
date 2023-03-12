package com.wreker.rickandmortyapp.model

import android.icu.text.IDNA

data class GetCharactersPageResponse(
    val info : Info?,
    val results : List<GetCharacterByIdResponse> = emptyList()
)

