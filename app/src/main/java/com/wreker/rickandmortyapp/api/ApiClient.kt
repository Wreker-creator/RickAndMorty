package com.wreker.rickandmortyapp.api

import com.wreker.rickandmortyapp.model.GetCharacterByIdResponse
import com.wreker.rickandmortyapp.model.GetCharactersPageResponse
import com.wreker.rickandmortyapp.model.GetEpisodeByIdResponse
import com.wreker.rickandmortyapp.model.GetEpisodeByPageResponse
import com.wreker.rickandmortyapp.tools.RickAndMortyResponse
import retrofit2.Response

class ApiClient(private val rickAndMortyService : RickAndMortyApi) {

    suspend fun getCharacterById(characterId : Int) : RickAndMortyResponse<GetCharacterByIdResponse>{
        return safeApiCall { rickAndMortyService.getCharacterById(characterId) }
    }

    suspend fun getCharactersPage(pageIndex : Int) : RickAndMortyResponse<GetCharactersPageResponse>{
        return safeApiCall { rickAndMortyService.getCharactersPage(pageIndex)}
    }

    suspend fun getEpisodeById(episodeId : Int) : RickAndMortyResponse<GetEpisodeByIdResponse>{
        return safeApiCall { rickAndMortyService.getEpisodeById(episodeId)}
    }

    suspend fun getEpisodeByRange(episodeRange : String) : RickAndMortyResponse<List<GetEpisodeByIdResponse>>{
        return safeApiCall { rickAndMortyService.getEpisodesByRange(episodeRange) }
    }

    suspend fun getEpisodesPage(pageIndex : Int) : RickAndMortyResponse<GetEpisodeByPageResponse>{
        return safeApiCall { rickAndMortyService.getEpisodesPage(pageIndex) }
    }

    //so here we go and define an inline function called safeApiCall that will accept a function
    //pointer to something that will return a response of type T and then we will go ahead
    //and return a RickAndMortyResponse [(our custom response to handle unsuccessful api calls)]
    //of that same type, but we will return it from a try/catch block

    //this where we handle the network request successfully
    //basically we use this inline function to wrap our response from thw network call
    //to handle the success and failure of the api call.
    private inline fun <T> safeApiCall(apiCall : () -> Response<T>) : RickAndMortyResponse<T>{
        return try {
            RickAndMortyResponse.success(apiCall.invoke())
        }catch (e : Exception){
            RickAndMortyResponse.failure(e)
        }
    }

}