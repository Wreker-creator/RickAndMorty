package com.wreker.rickandmortyapp.characters

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.wreker.rickandmortyapp.api.RetrofitInstance
import com.wreker.rickandmortyapp.domain.mapper.CharacterMapper
import com.wreker.rickandmortyapp.domain.model.Character
import com.wreker.rickandmortyapp.episode.EpisodesUiModel
import com.wreker.rickandmortyapp.repository.Repository

class CharactersPagingSource(
    private val repository: Repository
) : PagingSource<Int, Character>(){
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Character> {

        val pageNumber = params.key?:1
        val pageRequest = RetrofitInstance.apiClient.getCharactersPage(pageNumber)
        pageRequest.exception?.let {
            return LoadResult.Error(it)
        }

        return LoadResult.Page(
            data = pageRequest.body.results.map {response ->
                CharacterMapper.buildFrom(response)
            },
            prevKey = null,
            nextKey = getPageIndexFromNext(pageRequest.body.info?.next)
        )

    }

    override fun getRefreshKey(state: PagingState<Int, Character>): Int? {
        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(1)
                ?:state.closestPageToPosition(it)?.nextKey?.minus(1)
        }
    }

    private fun getPageIndexFromNext(next : String?) : Int?{
        return next?.split("?page=")?.get(1)?.toInt()
    }


}