package com.wreker.rickandmortyapp.repository

import com.wreker.rickandmortyapp.api.RetrofitInstance
import com.wreker.rickandmortyapp.model.GetCharacterByIdResponse
import com.wreker.rickandmortyapp.model.GetCharactersPageResponse

class Repository {

    suspend fun getCharacterById(characterId : Int) : GetCharacterByIdResponse?{
        val request = RetrofitInstance.apiClient.getCharacterById(characterId)

        if(request.failed){
            return null
        }

        if(!request.data!!.isSuccessful){
            return null
        }

        return request.body
    }

    suspend fun getCharactersPage(pageIndex : Int): GetCharactersPageResponse? {
        val request = RetrofitInstance.apiClient.getCharactersPage(pageIndex)

        if(request.failed){
            return null
        }

        return request.body

    }

}