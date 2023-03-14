package com.wreker.rickandmortyapp.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.wreker.rickandmortyapp.api.RickAndMortyCache
import com.wreker.rickandmortyapp.characters.CharactersDataSourceFactory
import com.wreker.rickandmortyapp.model.GetCharacterByIdResponse
import com.wreker.rickandmortyapp.repository.Repository
import com.wreker.rickandmortyapp.tools.Constants
import kotlinx.coroutines.launch
import com.wreker.rickandmortyapp.domain.model.Character
import com.wreker.rickandmortyapp.domain.model.Episode
import com.wreker.rickandmortyapp.episode.EpisodePagingSource
import com.wreker.rickandmortyapp.episode.EpisodesUiModel
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

    //this fetches the data in the form of pages, where we have a set page size and a
    //set prefetch distance
    private val pageListConfig : PagedList.Config = PagedList.Config.Builder()
        .setPageSize(Constants.pageSize)
        .setPrefetchDistance(Constants.prefetchDistance)
        .build()

    private val dataSourceFactory = CharactersDataSourceFactory(viewModelScope, repository)
    val charactersPagedLiveData: LiveData<PagedList<GetCharacterByIdResponse>> =
        LivePagedListBuilder(
            dataSourceFactory, pageListConfig
        ).build()

    //this is basically where we are handling when to add header and when to not.
    val flow = Pager(
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

                //if either of the model is header we dont need to add them
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
    }