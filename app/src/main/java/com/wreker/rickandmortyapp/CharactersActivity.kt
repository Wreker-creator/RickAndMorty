package com.wreker.rickandmortyapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.wreker.rickandmortyapp.databinding.ActivityCharactersBinding
import com.wreker.rickandmortyapp.epoxy.CharactersListPagingEpoxyController
import com.wreker.rickandmortyapp.tools.Constants
import com.wreker.rickandmortyapp.viewModel.CharactersViewModel

class CharactersActivity : AppCompatActivity() {

    private lateinit var binding : ActivityCharactersBinding
    private val epoxyController = CharactersListPagingEpoxyController(::onCharacterSelected)

    private val viewModel : CharactersViewModel by lazy {
        ViewModelProvider(this)[CharactersViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCharactersBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.charactersPagedLiveData.observe(this){pagedList ->
            epoxyController.submitList(pagedList)
        }

        binding.epoxyRecyclerView.setController(epoxyController)

    }

    private fun onCharacterSelected(characterId : Int){
        val intent = Intent(this@CharactersActivity, MainActivity::class.java)
        intent.putExtra(Constants.INTENT_EXTRA_CHARACTER_ID, characterId)
        startActivity(intent)
    }

}