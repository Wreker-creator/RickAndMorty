package com.wreker.rickandmortyapp.epoxy

import com.airbnb.epoxy.EpoxyController
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.squareup.picasso.Picasso
import com.wreker.rickandmortyapp.R
import com.wreker.rickandmortyapp.databinding.ModelCharacterDetailsDataPointBinding
import com.wreker.rickandmortyapp.databinding.ModelCharacterDetailsHeaderBinding
import com.wreker.rickandmortyapp.databinding.ModelCharacterDetailsImageBinding
import com.wreker.rickandmortyapp.model.GetCharacterByIdResponse

class CharacterDetailsEpoxyController : EpoxyController() {

    //we will need to change up the data we are passing in the live data to accomplish the
    //different states that we want i.e loading and success

    var isLoading : Boolean = true
    set(value) {
        field = value
        if(field){
            //if it is loading we will go ahead and show our loading ui is basically what's
            // happening here.
            requestModelBuild()
        }
    }

    var characterResponse : GetCharacterByIdResponse? = null
    set(value) {
        field = value
        if(field!=null){
            isLoading = false
            requestModelBuild()
        }
    }

    override fun buildModels() {

        if(isLoading){
            //showLoadingState
            LoadingEpoxyModel().id("loading").addTo(this)
            return
        }

        if(characterResponse == null){
            //todo error state
            return
        }

        //add header model
        HeaderEpoxyModel(
            name = characterResponse!!.name.toString(),
            gender = characterResponse!!.gender.toString(),
            status = characterResponse!!.status.toString()
        ).id("header").addTo(this)


        //add image model
        ImageEpoxyModel(
            imageUrl = characterResponse!!.image.toString()
        ).id("image").addTo(this)


        //add data entry points model
        DataPointEpoxyModel(
            title = "Origin",
            description = characterResponse!!.origin!!.name.toString()
        ).id("data_entry1").addTo(this)

        DataPointEpoxyModel(
            title = "Species",
            description = characterResponse!!.species.toString()
        ).id("data_entry2").addTo(this)

    }

    //here we take name gender and status as string to build the header,
    //
    data class HeaderEpoxyModel(
        val name : String,
        val gender : String,
        val status : String
    ) : ViewBindingKotlinModel<ModelCharacterDetailsHeaderBinding>(R.layout.model_character_details_header){

        override fun ModelCharacterDetailsHeaderBinding.bind() {
            ttvName.text = name
            ttvAlive.text = status

            if(gender.equals("male", true)){
                imgGender.setImageResource(R.drawable.ic_male)
            }else{
                imgGender.setImageResource(R.drawable.ic_female)
            }

        }

    }

    data class ImageEpoxyModel(
        val imageUrl : String
    ) : ViewBindingKotlinModel<ModelCharacterDetailsImageBinding>(R.layout.model_character_details_image){

        override fun ModelCharacterDetailsImageBinding.bind() {

//            val requestOptions: RequestOptions = RequestOptions()
//                .placeholder(R.mipmap.ic_launcher)
//                .error(R.mipmap.ic_launcher)
//
//            //load image with glide
//            imgCharacter.l
            Picasso.get().load(imageUrl).into(imgCharacter)

        }

    }

    data class DataPointEpoxyModel(
        val title : String,
        val description : String
    ) : ViewBindingKotlinModel<ModelCharacterDetailsDataPointBinding>(R.layout.model_character_details_data_point){

        override fun ModelCharacterDetailsDataPointBinding.bind() {
            ttvTitle.text = title
            ttvDescription.text = description
        }

    }

}