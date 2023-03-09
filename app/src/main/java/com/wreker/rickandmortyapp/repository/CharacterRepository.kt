package com.wreker.rickandmortyapp.repository

import com.wreker.rickandmortyapp.api.RetrofitInstance
import com.wreker.rickandmortyapp.model.GetCharactersPageResponse

class CharacterRepository {

    suspend fun getCharactersPage(pageIndex : Int): GetCharactersPageResponse? {
        val request = RetrofitInstance.apiClient.getCharactersPage(pageIndex)

        if(request.failed){
            return null
        }

        return request.body

    }

}