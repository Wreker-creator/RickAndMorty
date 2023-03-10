package com.wreker.rickandmortyapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.wreker.rickandmortyapp.NavGraphActivity
import com.wreker.rickandmortyapp.R
import com.wreker.rickandmortyapp.databinding.FragmentCharacterDetailsBinding
import com.wreker.rickandmortyapp.epoxy.CharacterDetailsEpoxyController
import com.wreker.rickandmortyapp.tools.toast
import com.wreker.rickandmortyapp.viewModel.ViewModel


class CharacterDetailsFragment : Fragment(R.layout.fragment_character_details) {

    private var _binding : FragmentCharacterDetailsBinding?=null
    private val binding get() = _binding!!
    private lateinit var viewModel : ViewModel

    private val epoxyController = CharacterDetailsEpoxyController()
    private val safeArgs : CharacterDetailsFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentCharacterDetailsBinding.inflate(layoutInflater, container, false)
        viewModel = (activity as NavGraphActivity).viewModel1
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.characterByIdLiveData.observe(viewLifecycleOwner) { character ->
            epoxyController.character = character

            if (character == null) {
                activity?.toast("Unsuccessful network call")
                return@observe
            }
        }

        viewModel.refreshCharacter(safeArgs.characterId)
        binding.epoxyRecyclerView.setControllerAndBuildModels(epoxyController)

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}