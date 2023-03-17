package com.wreker.rickandmortyapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.wreker.rickandmortyapp.databinding.FragmentEpisodeDetailsBinding
import com.wreker.rickandmortyapp.epoxy.EpisodeDetailsEpoxyController
import com.wreker.rickandmortyapp.viewModel.ViewModel

class EpisodeDetailsBottomSheetFragment : BottomSheetDialogFragment(){

    private var _binding : FragmentEpisodeDetailsBinding?=null
    private val binding get() = _binding!!

    private val viewModel : ViewModel by viewModels()
    private val safeArgs : EpisodeDetailsBottomSheetFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEpisodeDetailsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.episodeLiveData.observe(viewLifecycleOwner){episode ->
            if(episode == null){
                //todo handle error
                return@observe
            }

            binding.apply {

                ttvEpisodeName.text = episode.name
                ttvEpisodeNumber.text = episode.getFormattedSeason().toString()
                ttvEpisodeAirDate.text = episode.airDate

                binding.epoxyRecyclerView.setControllerAndBuildModels(
                    EpisodeDetailsEpoxyController(episode.characterList)
                )

            }

        }
        viewModel.fetchEpisode(safeArgs.episodeId)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}