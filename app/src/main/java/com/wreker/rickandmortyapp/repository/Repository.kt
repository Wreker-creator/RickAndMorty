package com.wreker.rickandmortyapp.repository

import com.wreker.rickandmortyapp.api.RetrofitInstance
import com.wreker.rickandmortyapp.domain.mapper.CharacterMapper
import com.wreker.rickandmortyapp.model.GetCharacterByIdResponse
import com.wreker.rickandmortyapp.model.GetCharactersPageResponse
import com.wreker.rickandmortyapp.model.GetEpisodeByIdResponse

class Repository {

    suspend fun getCharacterById(characterId : Int) : com.wreker.rickandmortyapp.domain.model.Character?{
        val request = RetrofitInstance.apiClient.getCharacterById(characterId)

        if(request.failed){
            return null
        }

        if(!request.data!!.isSuccessful){
            return null
        }

        val networkEpisodes = getEpisodesFromCharacterResponse(request.body)

        return CharacterMapper.buildFrom(
            response = request.body,
            episodes = networkEpisodes)
    }

    suspend fun getCharactersPage(pageIndex : Int): GetCharactersPageResponse? {
        val request = RetrofitInstance.apiClient.getCharactersPage(pageIndex)

        if(request.failed){
            return null
        }

        return request.body

    }

    private suspend fun getEpisodesFromCharacterResponse(
        response : GetCharacterByIdResponse) : List<GetEpisodeByIdResponse>{

        val episodeRange = response.episode?.map {
            it.substring(startIndex = it.lastIndexOf("/") + 1)
        }.toString()

        //to explain what's happening here we need to take a look at the response we get from
        //getCharacterByIdResponse, here we have an episode field which returns a string array of
        //the names of all the episodes where the particular character appears.

        //we can use the episode Id from the string present in that array and make another network
        //call to get the details of that episode. This network call will require a query of the
        //type "[1,2,3]" which is a list on which .toString() was called.

        //hence we use map function to get the substring of the episode string present in the array
        //of episodes we get from the getCharacterByIdResponse to create a list of all the variables
        //we create a substring from the string and take the last index where "/" appears and take
        //the next index which give us the episodeId and we call .toString() on it.

        //after that we make the network request

        val request = RetrofitInstance.apiClient.getEpisodeByRange(episodeRange = episodeRange)

        if(request.failed){
            return emptyList()
        }

        return request.body

    }


}