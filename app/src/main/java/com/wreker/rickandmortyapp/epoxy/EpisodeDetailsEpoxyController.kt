package com.wreker.rickandmortyapp.epoxy

import com.airbnb.epoxy.EpoxyController
import com.squareup.picasso.Picasso
import com.wreker.rickandmortyapp.R
import com.wreker.rickandmortyapp.domain.model.Character
import com.wreker.rickandmortyapp.databinding.ModelCharacterListItemSquareBinding

class EpisodeDetailsEpoxyController
    (private val characterList : List<Character>): EpoxyController() {

    override fun buildModels() {
        characterList.forEach{character ->
            CharacterEpoxyModel(
                imageUrl = character.image.toString(),
                name = character.name.toString()
            ).id(character.id).addTo(this)
        }
    }

    data class CharacterEpoxyModel(
        val imageUrl : String,
        val name : String
    ) : ViewBindingKotlinModel<ModelCharacterListItemSquareBinding>(R.layout.model_character_list_item_square){

        override fun ModelCharacterListItemSquareBinding.bind() {
            Picasso.get().load(imageUrl).into(characterImageView)
            characterNameTextView.text = name
        }

    }

}