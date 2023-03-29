package com.wreker.rickandmortyapp.characterSearch

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.wreker.rickandmortyapp.api.RetrofitInstance
import com.wreker.rickandmortyapp.domain.mapper.CharacterMapper
import com.wreker.rickandmortyapp.domain.model.Character
import com.wreker.rickandmortyapp.episode.EpisodesUiModel
import kotlin.reflect.jvm.internal.impl.descriptors.Visibilities.Local

class  CharacterSearchPagingSource
    (private val userSearch : String,
    private val localException : (Exception) -> Unit): PagingSource<Int, Character>(){


    sealed class LocalException() : Exception(){
        object EmptySearch : LocalException()
        object NoResult : LocalException()
    }

    override suspend fun load(params: LoadParams<Int>): PagingSource.LoadResult<Int, Character> {

        if(userSearch.isEmpty()){
            val exception = LocalException.EmptySearch
            localException(exception)
            return LoadResult.Error(exception)
        }

        val pageNumber = params.key ?: 1
        val previousPage = if(pageNumber == 1) null else pageNumber - 1

        val request = RetrofitInstance.apiClient.getCharactersPage(characterName = userSearch, pageIndex = pageNumber)

        request.exception?.let {
            return PagingSource.LoadResult.Error(it)
        }

        return PagingSource.LoadResult.Page(
            data = request.body.results.map {
                CharacterMapper.buildFrom(it)
            }?: emptyList(),
            prevKey = previousPage,
            nextKey = getPageIndexFromNext(request.body.info?.next)
        )

    }

    override fun getRefreshKey(state: PagingState<Int, Character>): Int? {
        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(1)
                ?:state.closestPageToPosition(it)?.nextKey?.minus(1)
        }
    }

    private fun getPageIndexFromNext(next : String?) : Int?{
        if(next == null){
            return null
        }

        val remainder = next.substringAfter("page=")
        val finalIndex = if(remainder.contains('&')){
            remainder.indexOfFirst { it == '&' }
        }else{
            remainder.length
        }

        return remainder.substring(0, finalIndex).toIntOrNull()

    }

}