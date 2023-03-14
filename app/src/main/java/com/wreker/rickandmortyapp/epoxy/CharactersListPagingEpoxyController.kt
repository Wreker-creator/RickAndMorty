package com.wreker.rickandmortyapp.epoxy

import com.airbnb.epoxy.EpoxyModel
import com.airbnb.epoxy.paging.PagedListEpoxyController
import com.squareup.picasso.Picasso
import com.wreker.rickandmortyapp.R
import com.wreker.rickandmortyapp.databinding.ModelCharacterItemBinding
import com.wreker.rickandmortyapp.databinding.ModelCharacterListTitleBinding
import com.wreker.rickandmortyapp.model.GetCharacterByIdResponse
import java.util.*

class CharactersListPagingEpoxyController(
   private val onCharacterSelected: (Int) -> Unit
) : PagedListEpoxyController<GetCharacterByIdResponse>() {

    //this function is used when we need to inflate the epoxy recycler view with data
    //so that it acts like a normal recycler view

    override fun buildItemModel(
        currentPosition: Int,
        item: GetCharacterByIdResponse?
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

    //we removed the ability to add headers because the data we were getting was unsorted
    //the main reason for it being unsorted was simply because we were not fetching the entire
    //data at once but rather in pages format, so if the response we got had character starting from
    //A, it would be followed up by other letter characters alphabetically, but after a while we
    //would see the characters from letter b again, different characters but because we did not
    //fetch the entire data at once we cant sort the data alphabetically and that made header with
    //alphabets useless.
    data class CharacterGridTitleEpoxyModel(
        val title : String
    ) : ViewBindingKotlinModel<ModelCharacterListTitleBinding>(R.layout.model_character_list_title){

        override fun ModelCharacterListTitleBinding.bind() {
            textView.text = title.trim()
        }

        override fun getSpanSize(totalSpanCount: Int, position: Int, itemCount: Int): Int {
            return totalSpanCount
        }

    }

}