package com.wreker.rickandmortyapp.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.wreker.rickandmortyapp.characterSearch.CharacterSearchPagingSource
import com.wreker.rickandmortyapp.episode.EpisodePagingSource
import com.wreker.rickandmortyapp.tools.Constants
import com.wreker.rickandmortyapp.tools.Event

class CharacterSearchViewModel : ViewModel(){

    private var currentUserSearch = ""
    private var pagingSource : CharacterSearchPagingSource?=null
    get() {

        if(field == null || field?.invalid == true){
            field = CharacterSearchPagingSource(currentUserSearch){exception->
                _localExceptionEventLiveData.postValue(Event(exception))
            }
        }

        return field

    }

    val flow = Pager(
        PagingConfig(
            pageSize = Constants.pageSize,
            prefetchDistance = Constants.prefetchDistance,
            enablePlaceholders = false
        )
    ){
        pagingSource!!
    }.flow.cachedIn(viewModelScope)

    //For error handling
    /*
    the google recommended approach to handle one time events
     */
    private val _localExceptionEventLiveData = MutableLiveData<Event<CharacterSearchPagingSource.LocalException>>()
    val localExceptionLiveData : LiveData<Event<CharacterSearchPagingSource.LocalException>> = _localExceptionEventLiveData

    fun submitQuery(currentText: String) {
        currentUserSearch = currentText.toString()
        pagingSource?.invalidate()
    }


}