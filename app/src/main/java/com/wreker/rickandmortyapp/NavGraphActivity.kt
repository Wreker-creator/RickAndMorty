package com.wreker.rickandmortyapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.wreker.rickandmortyapp.databinding.ActivityNavGraphBinding
import com.wreker.rickandmortyapp.viewModel.ViewModel

class NavGraphActivity : AppCompatActivity() {

    val viewModel1 : ViewModel by lazy {
        ViewModelProvider(this)[ViewModel::class.java]
    }

    private lateinit var binding : ActivityNavGraphBinding
    private lateinit var appBarConfiguration : AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNavGraphBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController

        appBarConfiguration = AppBarConfiguration(

            //we declared top level id's here so that we dont get the back button icon when we
            // switch fragments. This indicates that both of these are top level id's and dont
            //have parents to which they would go back to when clicked on on switched to.
            topLevelDestinationIds = setOf(
                R.id.characterListFragment,
                R.id.episodeListFragment,
                R.id.characterSearchFragment
            ),
            drawerLayout = binding.drawerLayout
        )
        setupActionBarWithNavController(navController = navController, configuration = appBarConfiguration)

        binding.navView.setupWithNavController(navController)
        //this is because when we start the app we dont get the highlight for characters menu item
        //even though we are showing it so instead what we have done here is that we programmatically
        //set the start destination as checked item.
        binding.navView.setCheckedItem(
            navController.graph.startDestinationId
        )

    }

    //handles the functioning for going back which shows up in the action bar at the top

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.fragmentContainerView)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }

}