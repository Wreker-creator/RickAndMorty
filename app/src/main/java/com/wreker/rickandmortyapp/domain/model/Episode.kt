package com.wreker.rickandmortyapp.domain.model

data class Episode(
    val id : Int? = 0,
    val name : String? = "",
    val airDate : String? = "",
    val seasonNumber : Int? = 0,
    val episodeNumber : Int? = 0
){

    //to get the season and episode in string format
    fun getFormattedSeason() : String{
        return "Season $seasonNumber Episode $episodeNumber"
    }

    fun getFormattedSeasonTruncated() : String{
        return "S.$seasonNumber E.$episodeNumber"
    }

}
