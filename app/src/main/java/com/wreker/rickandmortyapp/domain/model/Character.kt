package com.wreker.rickandmortyapp.domain.model


//Its not best practice to carry this response model to our domain layer
//If we go ahead and trace where the code is being called we will notice that when we call the
// function refreshCharacter, we actually make a network request and post the information to a
// live data to our domain or our viewLayer, the live data of the type GetCharacterById response.
// The position where it is being observed , we will notice that it is a network layer response,
// and we are using it directly in our domain layer which is not a good practice and doesn’t leave
// us with much room for further development. We are referencing the network response object
// in our code which is not the best practice and prevents further development as well.
//
//So we need to separate domain layer from network layer.
//
//With every domain and network model we need to create a mapper so we can simultaneously compress
// our network data to domain data. A bit of manipulation which will help us.

data class Character(
    val episodeList : List<Episode> = listOf(),
    val gender: String?,
    val id: Int?,
    val image: String?,
    val location: Location?,
    val name: String?,
    val origin: Origin?,
    val species: String?,
    val status: String?
){
    data class Origin(
        val name: String,
        val url: String
    )

    data class Location(
        val name: String,
        val url: String
    )

}

