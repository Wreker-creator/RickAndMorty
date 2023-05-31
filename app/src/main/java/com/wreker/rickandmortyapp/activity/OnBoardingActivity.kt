package com.wreker.rickandmortyapp.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.auth.api.identity.Identity
import com.wreker.rickandmortyapp.databinding.ActivityOnBoardingBinding
import com.wreker.rickandmortyapp.tools.GoogleAuthUiClient
import com.wreker.rickandmortyapp.tools.toast
import com.wreker.rickandmortyapp.viewModel.ViewModel

class OnBoardingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOnBoardingBinding

    private val googleAuthUiClient by lazy {
        GoogleAuthUiClient(
            context = applicationContext,
            oneTapClient = Identity.getSignInClient(applicationContext)
        )
    }

    val onBoardingViewModel : ViewModel by lazy {
        ViewModelProvider(this)[ViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen()

        binding = ActivityOnBoardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if(googleAuthUiClient.isSignedIn()){
            goToMainScreen()
            this.toast("Sign in Successful")
        }

        this.supportActionBar!!.hide()

    }

     fun goToMainScreen(){
        val intent = Intent(this@OnBoardingActivity, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun something(){
        val sharedPref = applicationContext.getSharedPreferences("signInState", Context.MODE_PRIVATE)
        sharedPref.registerOnSharedPreferenceChangeListener(SharedPreferences.OnSharedPreferenceChangeListener { _, key ->

            if(key.equals("finished")){
                if(googleAuthUiClient.isSignedIn()){
                    val intent = Intent(this@OnBoardingActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                    this.toast("checked the change in value")
                }
            }
        })
    }

    override fun onStop() {
        super.onStop()
    }

}