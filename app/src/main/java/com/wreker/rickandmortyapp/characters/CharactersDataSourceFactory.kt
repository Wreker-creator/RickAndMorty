package com.wreker.rickandmortyapp.characters

import android.util.Log
import androidx.paging.DataSource
import com.wreker.rickandmortyapp.model.GetCharacterByIdResponse
import com.wreker.rickandmortyapp.repository.Repository
import kotlinx.coroutines.CoroutineScope

class CharactersDataSourceFactory(
    private val coroutineScope: CoroutineScope,
    private val repository: Repository
) : DataSource.Factory<Int, GetCharacterByIdResponse>() {


    override fun create(): DataSource<Int, GetCharacterByIdResponse> {
        Log.e("TAG", "data source factory was created")
        return CharactersDataSource(coroutineScope, repository)
    }


}