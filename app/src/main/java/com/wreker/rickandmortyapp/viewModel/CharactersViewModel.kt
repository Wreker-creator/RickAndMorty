package com.wreker.rickandmortyapp.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.wreker.rickandmortyapp.characters.CharactersDataSource
import com.wreker.rickandmortyapp.characters.CharactersDataSourceFactory
import com.wreker.rickandmortyapp.model.GetCharacterByIdResponse
import com.wreker.rickandmortyapp.repository.CharacterRepository
import com.wreker.rickandmortyapp.tools.Constants

class CharactersViewModel : androidx.lifecycle.ViewModel(){

    private val repository = CharacterRepository()

    private val pageListConfig : PagedList.Config = PagedList.Config.Builder()
        .setPageSize(Constants.pageSize)
        .setPrefetchDistance(Constants.prefetchDistance)
        .build()

    private val dataSourceFactory = CharactersDataSourceFactory(viewModelScope, repository)
    val charactersPagedLiveData: LiveData<PagedList<GetCharacterByIdResponse>> =
        LivePagedListBuilder(
            dataSourceFactory, pageListConfig
        ).build()

}