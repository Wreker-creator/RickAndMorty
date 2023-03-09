package com.wreker.rickandmortyapp.tools

import retrofit2.Response

//why we are building this class has reason behind it.
//we no longer handle the network responses the way we used to.
//we switched from using Call<> to Response<> which works better with coroutines
//but doing that also disabled us from handling onFailure for network requests.
//hence we need to handle that on our own which warranted the need for this class

data class RickAndMortyResponse<T>(
    val status : Status,
    val data : Response<T>?,
    val exception: Exception?
) {

    companion object{
        fun <T> success(data : Response<T>) : RickAndMortyResponse<T>{
            return RickAndMortyResponse(
                status = Status.Success,
                data = data,
                exception = null
            )
        }

        fun <T> failure(exception : Exception) : RickAndMortyResponse<T>{
            return RickAndMortyResponse(
                status = Status.Failure,
                data = null,
                exception = exception
            )
        }

    }

    sealed class Status{
        object Success : Status()
        object Failure : Status()
    }

    val failed : Boolean
    get() = this.status == Status.Failure

    val success : Boolean
    get() = !failed && this.data?.isSuccessful == true

    val body : T
    get() = this.data!!.body()!!

}