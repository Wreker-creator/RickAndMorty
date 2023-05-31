package com.wreker.rickandmortyapp.activity

import android.content.Intent
import android.content.SharedPreferences
import android.os.Build.VERSION_CODES.P
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageButton
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.gms.auth.api.identity.Identity
import com.squareup.picasso.Picasso
import com.wreker.rickandmortyapp.R
import com.wreker.rickandmortyapp.databinding.ActivityNavGraphBinding
import com.wreker.rickandmortyapp.tools.Constants.Companion.firebaseAuth
import com.wreker.rickandmortyapp.tools.GoogleAuthUiClient
import com.wreker.rickandmortyapp.tools.toast
import com.wreker.rickandmortyapp.viewModel.ViewModel
import kotlinx.coroutines.launch
import org.checkerframework.checker.units.qual.s
import kotlin.math.sign

class MainActivity : AppCompatActivity() {

    val viewModel1 : ViewModel by lazy {
        ViewModelProvider(this)[ViewModel::class.java]
    }

    private val googleAuthUiClient by lazy {
        GoogleAuthUiClient(
            context = applicationContext,
            oneTapClient = Identity.getSignInClient(applicationContext)
        )
    }

    private lateinit var binding : ActivityNavGraphBinding
    private lateinit var appBarConfiguration : AppBarConfiguration
    lateinit var navController: NavController


    override fun onCreate(savedInstanceState: Bundle?) {

        installSplashScreen()

        super.onCreate(savedInstanceState)
        binding = ActivityNavGraphBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController = navHostFragment.navController

        appBarConfiguration = AppBarConfiguration(

            //we declared top level id's here so that we dont get the back button icon when we
            // switch fragments. This indicates that both of these are top level id's and dont
            //have parents to which they would go back to when clicked on on switched to.
            topLevelDestinationIds = setOf(
                R.id.characterListFragment,
                R.id.episodeListFragment,
                R.id.characterSearchFragment,
                R.id.signUpFragment,
                R.id.loginFragment,
                R.id.profileFragment
            ),
            drawerLayout = binding.drawerLayout
        )

        binding.navView.setupWithNavController(navController)

        setupActionBarWithNavController(navController = navController, configuration = appBarConfiguration)


        //this is because when we start the app we don't get the highlight for characters menu item
        //even though we are showing it so instead what we have done here is that we programmatically
        //set the start destination as checked item.
        binding.navView.setCheckedItem(
            navController.graph.startDestinationId
        )

        navDrawerFunctions()

    }

    private fun navDrawerFunctions(){
        val headerView = binding.navView.getHeaderView(0)
        val signedInUser = googleAuthUiClient.getSignedInUser()

        val profilePicture = headerView.findViewById<AppCompatImageView>(R.id.img_profile)

        if(signedInUser?.profilePictureUrl==null){
            Picasso.get().load(R.drawable.default_profile_picture).into(profilePicture)
        }else{
            Picasso.get().load(signedInUser.profilePictureUrl).into(profilePicture)
        }

        val logoutButton = headerView.findViewById<AppCompatImageButton>(R.id.btn_logout)
        logoutButton.setOnClickListener {
            lifecycleScope.launch {
                googleAuthUiClient.signOut()
                goToLoginScreen()
            }

        }

    }

    private fun goToLoginScreen(){
        val intent = Intent(this@MainActivity, OnBoardingActivity::class.java)
        startActivity(intent)
        finish()
    }

    //handles the functioning for going back which shows up in the action bar at the top

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.fragmentContainerView)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

}