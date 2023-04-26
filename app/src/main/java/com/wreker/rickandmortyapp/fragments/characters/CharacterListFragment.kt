package com.wreker.rickandmortyapp.fragments.characters

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.PagingData
import com.google.android.material.navigation.NavigationView
import com.wreker.rickandmortyapp.R
import com.wreker.rickandmortyapp.activity.MainActivity
import com.wreker.rickandmortyapp.databinding.FragmentCharacterListBinding
import com.wreker.rickandmortyapp.epoxy.CharacterListPagingEpoxyController
import com.wreker.rickandmortyapp.viewModel.ViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class CharacterListFragment : Fragment(R.layout.fragment_character_list) {

    private var _binding : FragmentCharacterListBinding?=null
    private val binding get() = _binding!!
    private lateinit var viewModel : ViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentCharacterListBinding.bind(view)

        viewModel = (activity as MainActivity).viewModel1

        val controller = CharacterListPagingEpoxyController(::onCharacterSelected)

        binding.epoxyRecyclerView.setControllerAndBuildModels(controller)

        lifecycleScope.launch {
            viewModel.characterListFlow.collectLatest {pagingData : PagingData<com.wreker.rickandmortyapp.domain.model.Character> ->
                controller.submitData(pagingData)
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun onCharacterSelected(characterId : Int){
        val directions =
            CharacterListFragmentDirections.actionCharacterListFragmentToCharacterDetailsFragment(
                characterId = characterId
            )
        findNavController().navigate(directions)
    }

    override fun onResume() {
        super.onResume()
        activity?.findViewById<NavigationView>(R.id.nav_view)?.setCheckedItem(
            R.id.characterListFragment
        )
    }

}