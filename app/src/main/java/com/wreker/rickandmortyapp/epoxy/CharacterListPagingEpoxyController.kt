package com.wreker.rickandmortyapp.epoxy

import androidx.paging.PagingData
import com.airbnb.epoxy.EpoxyModel
import com.airbnb.epoxy.paging.PagedListEpoxyController
import com.airbnb.epoxy.paging3.PagingDataEpoxyController
import com.squareup.picasso.Picasso
import com.wreker.rickandmortyapp.R
import com.wreker.rickandmortyapp.databinding.ModelCharacterItemBinding
import com.wreker.rickandmortyapp.databinding.ModelCharacterListTitleBinding
import com.wreker.rickandmortyapp.model.GetCharacterByIdResponse
import java.util.*

class CharacterListPagingEpoxyController(
    private val onCharacterSelected: (Int) -> Unit
) : PagingDataEpoxyController<com.wreker.rickandmortyapp.domain.model.Character>() {

    override fun buildItemModel(
        currentPosition: Int,
        item: com.wreker.rickandmortyapp.domain.model.Character?
    ): EpoxyModel<*> {

        return CharacterGridItemEpoxyModel(
            characterId = item?.id!!,
            imageUrl = item.image.toString(),
            characterName = item.name.toString(),
            onCharacterSelected = onCharacterSelected
        ).id("character_${item.id}")

    }

    override fun addModels(models: List<EpoxyModel<*>>) {

        if(models.isEmpty()){
            LoadingEpoxyModel().id("loading").addTo(this)
            return
        }

        super.addModels(models)

    }

    data class CharacterGridItemEpoxyModel(
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