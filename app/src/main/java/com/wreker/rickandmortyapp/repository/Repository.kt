package com.wreker.rickandmortyapp.repository

import com.google.firebase.auth.AuthResult
import com.wreker.rickandmortyapp.api.RetrofitInstance
import com.wreker.rickandmortyapp.api.RickAndMortyCache
import com.wreker.rickandmortyapp.domain.mapper.CharacterMapper
import com.wreker.rickandmortyapp.domain.mapper.EpisodeMapper
import com.wreker.rickandmortyapp.domain.model.Character
import com.wreker.rickandmortyapp.domain.model.Episode
import com.wreker.rickandmortyapp.model.*
import com.wreker.rickandmortyapp.tools.Constants.Companion.firebaseAuth
import com.wreker.rickandmortyapp.tools.Constants.Companion.firebaseDatabase
import com.wreker.rickandmortyapp.tools.Resource
import com.wreker.rickandmortyapp.tools.safeCall
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class Repository {

    suspend fun getCharacterById(characterId : Int) : Character?{

        //to check if the character network request has already been made or not
        //if yes then display the character already present as cache otherwise
        //make the network call and add the response to cache
        val cachedCharacter = RickAndMortyCache.characterMap[characterId]
        if(cachedCharacter !=null){
            return cachedCharacter
        }

        val request = RetrofitInstance.apiClient.getCharacterById(characterId)

        if(request.failed || !request.success){
            return null
        }

        val networkEpisodes = getEpisodesFromCharacterResponse(request.body)

        val character = CharacterMapper.buildFrom(
            response = request.body,
            episodes = networkEpisodes)

        RickAndMortyCache.characterMap[characterId] = character
        return character
    }

    suspend fun getCharactersPage(pageIndex : Int): GetCharactersPageResponse? {
        val request = RetrofitInstance.apiClient.getCharactersPage(pageIndex)

        if(request.failed || !request.success){
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

        if(request.failed || !request.success){
            return emptyList()
        }

        return request.body

    }

    private suspend fun getEpisodePage(pageIndex : Int): GetEpisodeByPageResponse?{
        val request = RetrofitInstance.apiClient.getEpisodesPage(pageIndex)

        if(request.failed || !request.success){
            return null
        }

        return request.body

    }

    suspend fun getEpisodeById(episodeId : Int) : Episode?{
        val request = RetrofitInstance.apiClient.getEpisodeById(episodeId)

        if(request.failed || !request.success){
            return null
        }

        val characterList = getCharactersFromEpisodeResponse(request.body)
        return EpisodeMapper.buildFrom(
            networkEpisode = request.body,
            networkCharacters = characterList
        )

    }

    private suspend fun getCharactersFromEpisodeResponse(
        episodeByIdResponse : GetEpisodeByIdResponse
    ) : List<GetCharacterByIdResponse>{
        val characterList = episodeByIdResponse.characters?.map {
            it.substring(startIndex = it.lastIndexOf("/")+1)
        }

        val characterRange = RetrofitInstance.apiClient.getCharactersByRange(characterRange = characterList.toString())
        if(characterRange.failed || !characterRange.success){
            return emptyList()
        }
        return characterRange.body

    }

//  The createUser function is a suspend function that has a return type of AuthResult which is wrapped in the Resource class that we created earlier. Inside this function, we make use of coroutine’s withContext and make sure that our coroutine runs in the Dispatchers.IO.
//
//  We then use the inline function - safeCall that we created in the util directory.
//
//  Inside the inline function:
//
//  We invoke firebaseAuth.createUserWithEmailAndPassword(email, password) and make sure we add the await() at the end. The result of the execution is stored in the variable result.
//  Next, we extract the uid of the newly created user and then create an object of the class User with all the respective arguments.
//  We then invoke databaseReference.child(uid).setValue(user).await() to store the user in Firebase Realtime database.
//  Lastly, we return a successful result.
    suspend fun createUser(user : User, userPassword : String) : Resource<AuthResult>{
        return withContext(Dispatchers.IO){
            safeCall {

                val registrationResult = firebaseAuth.createUserWithEmailAndPassword(user.email, userPassword).await()

                val userId = registrationResult.user?.uid!!
                firebaseDatabase.child(userId).setValue(user).await()

                Resource.Success(registrationResult)

            }
        }
    }


//    Similar to the register function, in the login function we make sure that we add the await() at the end of firebaseAuth.signInWithEmailAndPassword(email, password).
    suspend fun login(email: String, password: String): Resource<AuthResult> {
        return withContext(Dispatchers.IO) {
            safeCall {
                val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
                Resource.Success(result)
            }
        }
    }

}