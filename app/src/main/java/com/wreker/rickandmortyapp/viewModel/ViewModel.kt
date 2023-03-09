package com.wreker.rickandmortyapp.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.wreker.rickandmortyapp.model.GetCharacterByIdResponse
import com.wreker.rickandmortyapp.repository.Repository
import kotlinx.coroutines.launch

class ViewModel : androidx.lifecycle.ViewModel(){

    private val repository = Repository()

    //the reason why we have a private live data object and a non-private one as well
    // is simply because we don't want whatever object is listening to response to change on
    //its own,so in here we have a private live data object which is mutable and acts as a listener
    //while we also have a non mutable live data object which can be accessed outside of the
    //viewModel class

    private val _characterByIdLiveData = MutableLiveData<GetCharacterByIdResponse?>()
    val characterByIdLiveData : LiveData<GetCharacterByIdResponse?> = _characterByIdLiveData

    fun refreshCharacter(characterId : Int){
        viewModelScope.launch {
            val response = repository.getCharacterById(characterId)
            _characterByIdLiveData.postValue(response)
        }
    }

}