package com.wreker.rickandmortyapp.epoxy

import com.airbnb.epoxy.EpoxyModel
import com.airbnb.epoxy.paging3.PagingDataEpoxyController
import com.wreker.rickandmortyapp.R
import com.wreker.rickandmortyapp.databinding.ModelCharacterListTitleBinding
import com.wreker.rickandmortyapp.databinding.ModelEpisodeItemBinding
import com.wreker.rickandmortyapp.domain.model.Episode
import com.wreker.rickandmortyapp.episode.EpisodesUiModel

class EpisodeListEpoxyController : PagingDataEpoxyController<EpisodesUiModel>() {
    override fun buildItemModel(currentPosition: Int, item: EpisodesUiModel?): EpoxyModel<*> {

        return when(item!!){
            is EpisodesUiModel.Item -> {
                val episode = (item as EpisodesUiModel.Item).episode
                EpisodeListItemEpoxyModel(
                    episode = episode,
                    onCLick = {episodeId ->

                    }
                ).id("episode_${episode.id}")
            }

            is EpisodesUiModel.Header -> {
                val header = (item as EpisodesUiModel.Header).text
                EpisodeListTitleEpoxyModel(header).id("header_${header}")
            }

        }

    }

    data class EpisodeListItemEpoxyModel(
        val episode : Episode,
        val onCLick : (Int) -> Unit
    ) : ViewBindingKotlinModel<ModelEpisodeItemBinding>(R.layout.model_episode_item){

        override fun ModelEpisodeItemBinding.bind() {
            ttvEpisodeName.text = episode.name + " (${episode.getFormattedSeasonTruncated()})"
            ttvAirDate.text = episode.airDate

            root.setOnClickListener { episode.id?.let { it1 -> onCLick(it1) } }

        }

    }


    data class EpisodeListTitleEpoxyModel(
        val text : String
    ) : ViewBindingKotlinModel<ModelCharacterListTitleBinding>(R.layout.model_character_list_title){
        override fun ModelCharacterListTitleBinding.bind() {
            textView.text = text
        }

    }

}