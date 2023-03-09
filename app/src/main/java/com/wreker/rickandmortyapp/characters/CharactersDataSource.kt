package com.wreker.rickandmortyapp.characters

import androidx.paging.PageKeyedDataSource
import com.wreker.rickandmortyapp.model.GetCharacterByIdResponse
import com.wreker.rickandmortyapp.repository.Repository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class CharactersDataSource(
    private val coroutineScope: CoroutineScope,
    private val repository: Repository
) : PageKeyedDataSource<Int, GetCharacterByIdResponse>(){

    //our functionality here only demands the use if load Initial and after so we do nothing in
    // load before

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, GetCharacterByIdResponse>
    ) {

        coroutineScope.launch {

            val page = repository.getCharactersPage(1)

            if(page == null){
                callback.onResult(emptyList(), null, null)
                return@launch
            }

            callback.onResult(page.results, null, getPageIndexFromNext(page.info!!.next))

        }

    }

    override fun loadAfter(
        params: LoadParams<Int>,
        callback: LoadCallback<Int, GetCharacterByIdResponse>
    ) {

        coroutineScope.launch {

            val page = repository.getCharactersPage(params.key)

            if (page == null) {
                callback.onResult(emptyList(), null)
                return@launch
            }

            callback.onResult(page.results, getPageIndexFromNext(page.info!!.next))

        }

    }

    override fun loadBefore(
        params: LoadParams<Int>,
        callback: LoadCallback<Int, GetCharacterByIdResponse>
    ) {
        //nothing to do
    }

    //our helper function that helps extract the necessary data from our string response we get
    //from our apiCall
    //we get the response as such {"next": "https://rickandmortyapi.com/api/character/?page=2",}
    // we only need the page number from this string and hence we split the response to only get
    //page number which may or may not be null as there is a set number of characters there wont
    //be infinite scroll so at some point there wont be a next page, to handle that we do null check

    // also the response we are getting from split function is an array so we need to get the second
    //element of the array as that contains the page number
    private fun getPageIndexFromNext(next : String?) : Int?{
        return next?.split("?page=")?.get(1)?.toInt()
    }


}