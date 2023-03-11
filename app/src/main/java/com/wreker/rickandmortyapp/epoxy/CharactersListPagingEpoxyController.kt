package com.wreker.rickandmortyapp.epoxy

import com.airbnb.epoxy.EpoxyModel
import com.airbnb.epoxy.paging.PagedListEpoxyController
import com.squareup.picasso.Picasso
import com.wreker.rickandmortyapp.R
import com.wreker.rickandmortyapp.databinding.ModelCharacterItemBinding
import com.wreker.rickandmortyapp.model.GetCharacterByIdResponse

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
        ).id(item.id)
    }

    override fun addModels(models: List<EpoxyModel<*>>) {

        if(models.isEmpty()){
            LoadingEpoxyModel().id("loading").addTo(this)
            return
        }

//        CharacterGridTitleEpoxyModel("Main Family")
//            .id("main_family_header")
//            .addTo(this)
//
//        super.addModels(models.subList(0, 5))
//
//        (models.subList(5, models.size) as List<CharacterGridItemEpoxyModel>).groupBy {
//            it.characterName[0].uppercaseChar()
//        }.forEach {mapEntry ->
//
//            val character = mapEntry.key.toString().uppercase(Locale.US)
//            CharacterGridTitleEpoxyModel(character)
//                .id(character)
//                .addTo(this)
//            super.addModels(mapEntry.value)
//        }

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

//    data class CharacterGridTitleEpoxyModel(
//        val title : String
//    ) : ViewBindingKotlinModel<ModelCharacterListTitleBinding>(R.layout.model_character_list_title){
//
//        override fun ModelCharacterListTitleBinding.bind() {
//            textView.text = title.trim()
//        }
//
//        override fun getSpanSize(totalSpanCount: Int, position: Int, itemCount: Int): Int {
//            return totalSpanCount
//        }
//
//    }

}