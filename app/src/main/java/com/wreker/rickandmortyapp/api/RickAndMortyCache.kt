package com.wreker.rickandmortyapp.api

import com.wreker.rickandmortyapp.domain.model.Character
import com.wreker.rickandmortyapp.domain.model.Episode
import com.wreker.rickandmortyapp.model.User

object RickAndMortyCache {

    val characterMap = mutableMapOf<Int, Character>()
    val user = User(
        displayName = null,
        firstName = null,
        lastName = null,
        email = "",
        phoneNumber = null,
        birthday = null,
        profileImage = null
    )

}