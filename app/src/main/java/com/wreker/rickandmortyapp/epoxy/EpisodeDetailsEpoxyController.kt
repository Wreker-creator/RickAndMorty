package com.wreker.rickandmortyapp.epoxy

import com.airbnb.epoxy.EpoxyController
import com.squareup.picasso.Picasso
import com.wreker.rickandmortyapp.R
import com.wreker.rickandmortyapp.domain.model.Character
import com.wreker.rickandmortyapp.databinding.ModelCharacterListItemSquareBinding

class EpisodeDetailsEpoxyController
    (private val characterList : List<Character>,
    private val onCharacterSelected :(Int) -> Unit): EpoxyController() {

    override fun buildModels() {
        characterList.forEach{character ->
            CharacterEpoxyModel(
                character, onCharacterSelected
            ).id(character.id).addTo(this)
        }
    }

    data class CharacterEpoxyModel(
        val character: com.wreker.rickandmortyapp.domain.model.Character,
        val onCharacterSelected: (Int) -> Unit
    ) : ViewBindingKotlinModel<ModelCharacterListItemSquareBinding>(R.layout.model_character_list_item_square){

        override fun ModelCharacterListItemSquareBinding.bind() {
            Picasso.get().load(character.image).into(characterImageView)
            characterNameTextView.text = character.name

            root.setOnClickListener {
                onCharacterSelected(character.id!!)
            }

        }

    }

}