package com.wreker.rickandmortyapp.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.google.firebase.auth.AuthResult
import com.wreker.rickandmortyapp.characters.CharactersPagingSource
import com.wreker.rickandmortyapp.repository.Repository
import com.wreker.rickandmortyapp.tools.Constants
import kotlinx.coroutines.launch
import com.wreker.rickandmortyapp.domain.model.Character
import com.wreker.rickandmortyapp.domain.model.Episode
import com.wreker.rickandmortyapp.episode.EpisodePagingSource
import com.wreker.rickandmortyapp.episode.EpisodesUiModel
import com.wreker.rickandmortyapp.model.User
import com.wreker.rickandmortyapp.tools.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map

class ViewModel : androidx.lifecycle.ViewModel(){

    private val repository = Repository()

    //the reason why we have a private live data object and a non-private one as well
    // is simply because we don't want whatever object is listening to response to change on
    //its own,so in here we have a private live data object which is mutable and acts as a listener
    //while we also have a non mutable live data object which can be accessed outside of the
    //viewModel class

    private var _characterByIdLiveData = MutableLiveData<Character?>()
    val characterByIdLiveData : LiveData<Character?> get() = _characterByIdLiveData

    fun fetchCharacter(characterId : Int) = viewModelScope.launch {
        val character = repository.getCharacterById(characterId)
        _characterByIdLiveData.postValue(character)
    }


    //this is basically where we are handling when to add header and when to not.
    val episodeListFlow = Pager(
        PagingConfig(
            pageSize = Constants.pageSize,
            prefetchDistance = Constants.prefetchDistance,
            enablePlaceholders = false
        )
    ){
        EpisodePagingSource(repository)
    }.flow
        .cachedIn(viewModelScope).map {
            it.insertSeparators { model: EpisodesUiModel?, model2: EpisodesUiModel? ->

                //of the model1 is null th.n we are at the first episode which is definitely of
                //season1 hence we hardcoded it here,
                if(model == null) {
                    return@insertSeparators EpisodesUiModel.Header("Season 1")
                }

                if(model2 == null){
                    return@insertSeparators null
                }

                //if either of the model is header we don't need to add them
                if(model is EpisodesUiModel.Header || model2 is EpisodesUiModel.Header){
                    return@insertSeparators null
                }

                val episode1 = (model as EpisodesUiModel.Item).episode
                val episode2 = (model2 as EpisodesUiModel.Item).episode

                //if the season number of model2 is not the same as that of model1 then we have
                //moved a season forward and we can add that as a header. The value of the header
                //will be the season number of model2 as it is the next episode.
                return@insertSeparators if(episode2.seasonNumber != episode1.seasonNumber) {
                    EpisodesUiModel.Header("Season ${episode2.seasonNumber}")
                }else {
                    null
                }
            }
        }

    val characterListFlow =  Pager(
        PagingConfig(
            pageSize = Constants.pageSize,
            prefetchDistance = Constants.prefetchDistance,
            enablePlaceholders = false
        )
    ){
        CharactersPagingSource(repository)
    }.flow


    private var _episodeLiveData = MutableLiveData<Episode?>()
    val episodeLiveData : LiveData<Episode?>get() = _episodeLiveData

    fun fetchEpisode(episodeId : Int){
        viewModelScope.launch {
            val episode = repository.getEpisodeById(episodeId)
            _episodeLiveData.postValue(episode)
        }
    }

    private val _userSignUpStatus = MutableLiveData<Resource<AuthResult>>()
    val userSignUpStatus: LiveData<Resource<AuthResult>> = _userSignUpStatus

    private val _userLoginStatus = MutableLiveData<Resource<AuthResult>>()
    val userLoginStatus: LiveData<Resource<AuthResult>> = _userLoginStatus

    fun createUser(user : User, userLoginPassword: String) {
        val error =
            if (userLoginPassword.isEmpty()) {
                "Empty Strings"
            } else null

        error?.let {
            _userSignUpStatus.postValue(Resource.Error(it))
            return
        }
        _userSignUpStatus.postValue(Resource.Loading())

        viewModelScope.launch(Dispatchers.Main) {
            val registerResult = repository.createUser(user, userPassword = userLoginPassword)
            _userSignUpStatus.postValue(registerResult)
        }
    }

    fun signInUser(userEmailAddress: String, userLoginPassword: String) {
        if (userEmailAddress.isEmpty() || userLoginPassword.isEmpty()) {
            _userLoginStatus.postValue(Resource.Error("Empty Strings"))
        } else {
            _userLoginStatus.postValue(Resource.Loading())
            viewModelScope.launch(Dispatchers.Main) {
                val loginResult = repository.login(userEmailAddress, userLoginPassword)
                _userLoginStatus.postValue(loginResult)
            }
        }
    }

}