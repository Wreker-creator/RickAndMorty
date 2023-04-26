package com.wreker.rickandmortyapp.fragments.episodes

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.PagingData
import com.wreker.rickandmortyapp.activity.MainActivity
import com.wreker.rickandmortyapp.R
import com.wreker.rickandmortyapp.databinding.FragmentEpisodeListBinding
import com.wreker.rickandmortyapp.episode.EpisodesUiModel
import com.wreker.rickandmortyapp.epoxy.EpisodeListEpoxyController
import com.wreker.rickandmortyapp.viewModel.ViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class EpisodeListFragment : Fragment(R.layout.fragment_episode_list) {

    private var _binding : FragmentEpisodeListBinding?=null
    private val binding get() = _binding!!

    private lateinit var viewModel: ViewModel
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentEpisodeListBinding.bind(view)
        viewModel = (activity as MainActivity).viewModel1

        val epoxyController = EpisodeListEpoxyController{ episodeId ->
            val navDirection =
                EpisodeListFragmentDirections.actionEpisodeListFragmentToEpisodeDetailsBottomSheetFragment(
                    episodeId = episodeId
                )

            findNavController().navigate(directions = navDirection)
        }

        binding.epoxyRecyclerView.setControllerAndBuildModels(epoxyController)

        lifecycleScope.launch {
            viewModel.episodeListFlow.collectLatest { pagingData : PagingData<EpisodesUiModel> ->
                epoxyController.submitData(pagingData)
            }
        }

    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}