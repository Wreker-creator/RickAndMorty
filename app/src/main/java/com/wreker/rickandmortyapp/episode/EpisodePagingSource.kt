package com.wreker.rickandmortyapp.episode

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.wreker.rickandmortyapp.api.RetrofitInstance
import com.wreker.rickandmortyapp.domain.mapper.EpisodeMapper
import com.wreker.rickandmortyapp.domain.model.Episode
import com.wreker.rickandmortyapp.repository.Repository

class EpisodePagingSource(
    private val repository: Repository
) : PagingSource<Int, EpisodesUiModel>(){

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, EpisodesUiModel> {
        val pageNumber = params.key ?: 1

        val pageRequest = RetrofitInstance.apiClient.getEpisodesPage(pageNumber)

        pageRequest.exception?.let {
            return LoadResult.Error(it)
        }

        return LoadResult.Page(
            data = pageRequest.body.results.map {response->
                //basically we are wrapping up our response to see if the we need to add a
                //header or not

                //here we are also MENTIONING that the response we got is an item and not a header.
                EpisodesUiModel.Item(EpisodeMapper.buildFrom(response))
            },
            prevKey = null,
            nextKey = getPageIndexFromNext(pageRequest.body.info?.next)//todo clean this up with network information
        )

    }

    override fun getRefreshKey(state: PagingState<Int, EpisodesUiModel>): Int? {
        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(1)
                ?:state.closestPageToPosition(it)?.nextKey?.minus(1)
        }
    }

    private fun getPageIndexFromNext(next : String?) : Int?{
        return next?.split("?page=")?.get(1)?.toInt()
    }

}