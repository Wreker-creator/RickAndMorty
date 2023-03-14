package com.wreker.rickandmortyapp.api

import com.wreker.rickandmortyapp.domain.model.Character
import com.wreker.rickandmortyapp.domain.model.Episode

object RickAndMortyCache {

    val characterMap = mutableMapOf<Int, Character>()

}