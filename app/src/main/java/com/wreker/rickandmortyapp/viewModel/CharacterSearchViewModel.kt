package com.wreker.rickandmortyapp.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.wreker.rickandmortyapp.characterSearch.CharacterSearchPagingSource
import com.wreker.rickandmortyapp.episode.EpisodePagingSource
import com.wreker.rickandmortyapp.tools.Constants

class CharacterSearchViewModel : ViewModel(){

    private var currentUserSearch = ""
    private var pagingSource : CharacterSearchPagingSource?=null
    get() {

        if(field == null || field?.invalid == true){
            field = CharacterSearchPagingSource(currentUserSearch){exception->
                Log.e("Local Exception", exception.toString())
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

    fun submitQuery(currentText: String) {
        currentUserSearch = currentText.toString()
        pagingSource?.invalidate()
    }


}