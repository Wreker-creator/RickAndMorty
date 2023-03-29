package com.wreker.rickandmortyapp.epoxy

import com.airbnb.epoxy.EpoxyModel
import com.airbnb.epoxy.paging3.PagingDataEpoxyController
import com.squareup.picasso.Picasso
import com.wreker.rickandmortyapp.R
import com.wreker.rickandmortyapp.databinding.ModelCharacterItemBinding
import com.wreker.rickandmortyapp.domain.model.Character

class CharacterSearchEpoxyController(
    private val onCharacterSelected: (Int) -> Unit
) : PagingDataEpoxyController<Character>() {

    override fun buildItemModel(currentPosition: Int, item: Character?): EpoxyModel<*> {
        return CharacterSearchGridItemEpoxyModel(
            characterId = item?.id!!,
            imageUrl = item.image.toString(),
            characterName = item.name.toString(),
            onCharacterSelected = onCharacterSelected
        ).id("characterSearch_id${item.id}")
    }

    override fun addModels(models: List<EpoxyModel<*>>) {

        if(models.isEmpty()){
            LoadingEpoxyModel().id("loading").addTo(this)
            return
        }

        super.addModels(models)

    }

    data class CharacterSearchGridItemEpoxyModel(
        val characterId : Int,
        val imageUrl : String,
        val characterName : String,
        val onCharacterSelected : (Int) -> Unit
    ) : ViewBindingKotlinModel<ModelCharacterItemBinding>(R.layout.model_character_item){

        override fun ModelCharacterItemBinding.bind() {
            Picasso.get().load(imageUrl).into(charactersImage)
            charactersName.text = characterName

            root.setOnClickListener {
                onCharacterSelected(characterId)
            }

        }

    }

}