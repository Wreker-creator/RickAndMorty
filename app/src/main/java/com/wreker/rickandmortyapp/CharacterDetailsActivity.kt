package com.wreker.rickandmortyapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.lifecycle.ViewModelProvider
import com.wreker.rickandmortyapp.databinding.ActivityMainBinding
import com.wreker.rickandmortyapp.epoxy.CharacterDetailsEpoxyController
import com.wreker.rickandmortyapp.tools.Constants
import com.wreker.rickandmortyapp.tools.toast
import com.wreker.rickandmortyapp.viewModel.ViewModel

class CharacterDetailsActivity : AppCompatActivity() {

    private val viewModel : ViewModel by lazy {
        ViewModelProvider(this)[ViewModel::class.java]
    }

    private val epoxyController = CharacterDetailsEpoxyController()

    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        viewModel.characterByIdLiveData.observe(this) { character ->
            epoxyController.character = character

            if (character == null) {
                this.toast("Unsuccessful network call")
                return@observe
            }
        }

        val id = intent.getIntExtra(Constants.INTENT_EXTRA_CHARACTER_ID, 1)
        viewModel.refreshCharacter(intent.getIntExtra(Constants.INTENT_EXTRA_CHARACTER_ID, 1))
        binding.epoxyRecyclerView.setControllerAndBuildModels(epoxyController)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            android.R.id.home ->{
                finish()
                true
            }else ->{
                super.onOptionsItemSelected(item)
            }
        }
    }

}