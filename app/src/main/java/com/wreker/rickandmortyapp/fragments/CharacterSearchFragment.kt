package com.wreker.rickandmortyapp.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.wreker.rickandmortyapp.R
import com.wreker.rickandmortyapp.databinding.FragmentCharacterSearchBinding
import com.wreker.rickandmortyapp.epoxy.CharacterSearchEpoxyController
import com.wreker.rickandmortyapp.viewModel.CharacterSearchViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class CharacterSearchFragment : Fragment(R.layout.fragment_character_search){

    private var _binding : FragmentCharacterSearchBinding?=null
    private val binding get() = _binding!!

    private val viewModel : CharacterSearchViewModel by viewModels()

    private var currentText = ""
    private val handler : Handler = Handler(Looper.getMainLooper())
    private val runnable = Runnable {
        viewModel.submitQuery(currentText)
        println("currentText")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentCharacterSearchBinding.bind(view)

        val epoxyController = CharacterSearchEpoxyController(::onCharacterSelected)

        binding.editText.doAfterTextChanged {
            currentText = it.toString()
            handler.removeCallbacks(runnable)
            handler.postDelayed(runnable, 500L)
        }

        binding.epoxyRecyclerView.setControllerAndBuildModels(epoxyController)

        lifecycleScope.launch {
            viewModel.flow.collectLatest {
                epoxyController.localException = null
                epoxyController.submitData(it)
            }
        }

        viewModel.localExceptionLiveData.observe(viewLifecycleOwner){event->

            event.getContent()?.let {localException ->
                epoxyController.localException = localException
            }

        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }

    private fun onCharacterSelected(characterId : Int){
        val navDirections = CharacterSearchFragmentDirections.actionCharacterSearchFragmentToCharacterDetailsFragment(characterId)
        findNavController().navigate(directions = navDirections)
    }


}