package com.wreker.rickandmortyapp.fragments.characters

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.wreker.rickandmortyapp.activity.MainActivity
import com.wreker.rickandmortyapp.R
import com.wreker.rickandmortyapp.databinding.FragmentCharacterDetailsBinding
import com.wreker.rickandmortyapp.epoxy.CharacterDetailsEpoxyController
import com.wreker.rickandmortyapp.tools.toast
import com.wreker.rickandmortyapp.viewModel.ViewModel


class CharacterDetailsFragment : Fragment(R.layout.fragment_character_details) {

    private var _binding : FragmentCharacterDetailsBinding?=null
    private val binding get() = _binding!!
    private lateinit var viewModel : ViewModel

    private var epoxyController = CharacterDetailsEpoxyController(::onEpisodeSelected)
    private val safeArgs : CharacterDetailsFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentCharacterDetailsBinding.bind(view)
        viewModel = (activity as MainActivity).viewModel1

        viewModel.fetchCharacter(safeArgs.characterId)

        binding.epoxyRecyclerView.setControllerAndBuildModels(epoxyController)

        viewModel.characterByIdLiveData.observe(viewLifecycleOwner) { character ->
            epoxyController.character = character

            if (character == null) {
                activity?.toast("Unsuccessful network call")
                //this here has been done so that in the case of an unsuccessful network call we
                //can come back to the main screen rather than keeping the user on a data less
                //screen.

                findNavController().navigateUp()
                return@observe
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun onEpisodeSelected(episodeId : Int){
        val directions =
            CharacterDetailsFragmentDirections.actionCharacterDetailsFragmentToEpisodeDetailsBottomSheetFragment(
                episodeId
            )
        findNavController().navigate(directions)
    }

}