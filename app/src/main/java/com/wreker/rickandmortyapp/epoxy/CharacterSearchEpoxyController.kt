package com.wreker.rickandmortyapp.epoxy

import com.airbnb.epoxy.EpoxyModel
import com.airbnb.epoxy.paging3.PagingDataEpoxyController
import com.squareup.picasso.Picasso
import com.wreker.rickandmortyapp.R
import com.wreker.rickandmortyapp.characterSearch.CharacterSearchPagingSource
import com.wreker.rickandmortyapp.databinding.ModelCharacterItemBinding
import com.wreker.rickandmortyapp.databinding.ModelLocalExceptionErrorStateBinding
import com.wreker.rickandmortyapp.domain.model.Character
import java.io.FileDescriptor
import kotlin.reflect.jvm.internal.impl.descriptors.Visibilities.Local

class CharacterSearchEpoxyController(
    private val onCharacterSelected: (Int) -> Unit
) : PagingDataEpoxyController<Character>() {

    var localException : CharacterSearchPagingSource.LocalException?=null
    set(value) {
        field = value
        if(localException!=null){
            requestForcedModelBuild()
            //we would not have items being built but addModels will execute
        }
    }

    override fun buildItemModel(currentPosition: Int, item: Character?): EpoxyModel<*> {
        return CharacterSearchGridItemEpoxyModel(
            characterId = item?.id!!,
            imageUrl = item.image.toString(),
            characterName = item.name.toString(),
            onCharacterSelected = onCharacterSelected
        ).id("characterSearch_id${item.id}")
    }

    override fun addModels(models: List<EpoxyModel<*>>) {

        localException?.let {
            LocalExceptionErrorStateModel(it).id("error_state").addTo(this)
            return
        }

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

    data class LocalExceptionErrorStateModel(
        val localException : CharacterSearchPagingSource.LocalException
    ) : ViewBindingKotlinModel<ModelLocalExceptionErrorStateBinding>(R.layout.model_local_exception_error_state){

        override fun ModelLocalExceptionErrorStateBinding.bind() {
            errorTitle.text = localException.title
            errorDescription.text = localException.description
        }

        override fun getSpanSize(totalSpanCount: Int, position: Int, itemCount: Int): Int {
            return totalSpanCount
        }
    }

}