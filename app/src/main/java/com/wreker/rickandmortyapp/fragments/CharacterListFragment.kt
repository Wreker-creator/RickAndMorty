package com.wreker.rickandmortyapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.wreker.rickandmortyapp.NavGraphActivity
import com.wreker.rickandmortyapp.R
import com.wreker.rickandmortyapp.databinding.FragmentCharacterListBinding
import com.wreker.rickandmortyapp.epoxy.CharactersListPagingEpoxyController
import com.wreker.rickandmortyapp.viewModel.ViewModel


class CharacterListFragment : Fragment(R.layout.fragment_character_list) {

    private var _binding : FragmentCharacterListBinding?=null
    private val binding get() = _binding!!
    private val epoxyController = CharactersListPagingEpoxyController(::onCharacterSelected)
    private lateinit var viewModel : ViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentCharacterListBinding.inflate(layoutInflater, container, false)
        viewModel = (activity as NavGraphActivity).viewModel1
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.charactersPagedLiveData.observe(viewLifecycleOwner){pagedList ->
            epoxyController.submitList(pagedList)
        }

        binding.epoxyRecyclerView.setController(epoxyController)

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun onCharacterSelected(characterId : Int){
        val directions = CharacterListFragmentDirections.actionCharacterListFragmentToCharacterDetailsFragment(characterId = characterId)
        findNavController().navigate(directions)
    }

}