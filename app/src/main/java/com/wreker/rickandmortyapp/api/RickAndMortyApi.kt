package com.wreker.rickandmortyapp.api

import com.wreker.rickandmortyapp.model.GetCharacterByIdResponse
import com.wreker.rickandmortyapp.model.GetCharactersPageResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RickAndMortyApi {

    //Get request - > because we want something from the server we are sending a get request
    //Suspend fun - > because we want to make all our requests in the background thread so
    //                that we don't block the main thread. So we can do that using coroutines
    //                whose syntax is suspend fun. By doing this we make an synchronous function

    //now if we don't take any kind of input to the request we will keep on getting the same
    //response. To avoid that we can take some kind of input to the http so we can get the
    //required result.

    @GET("character/{character_id}")
    suspend fun getCharacterById(
        @Path("character_id") character_id: Int
    ) : Response<GetCharacterByIdResponse>

    @GET("character")
    suspend fun getCharactersPage(
        @Query("page") pageIndex : Int
    ) : Response<GetCharactersPageResponse>

}