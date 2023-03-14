package com.wreker.rickandmortyapp.episode

import com.wreker.rickandmortyapp.domain.model.Episode


//to depict different types of ui models
sealed class EpisodesUiModel {

    class Item(val episode: Episode) : EpisodesUiModel()
    class Header(val text : String) : EpisodesUiModel()

}