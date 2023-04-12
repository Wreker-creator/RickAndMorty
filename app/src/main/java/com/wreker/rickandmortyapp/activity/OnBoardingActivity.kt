package com.wreker.rickandmortyapp.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.wreker.rickandmortyapp.databinding.ActivityOnBoardingBinding
import com.wreker.rickandmortyapp.tools.GoogleAuthUiClient
import com.wreker.rickandmortyapp.tools.toast
import kotlinx.coroutines.launch

class OnBoardingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOnBoardingBinding

    private val googleAuthUiClient by lazy {
        GoogleAuthUiClient(
            context = applicationContext,
            oneTapClient = Identity.getSignInClient(applicationContext)
        )
    }

    private val launcher = registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()){

            result ->
        if(result.resultCode == RESULT_OK){

            lifecycleScope.launch {
                googleAuthUiClient.signInWithIntent(
                    intent = result.data ?: return@launch
                )
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnBoardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSignIn.setOnClickListener {

            lifecycleScope.launch {
                val signInIntentSender = googleAuthUiClient.signIn()
                launcher.launch(
                    IntentSenderRequest.Builder(
                        signInIntentSender ?: return@launch
                    ).build()
                )

            }

        }

        something()

        if(googleAuthUiClient.isSignedIn()){
            goToMainScreen()
            this.toast("Sign in Successful")
        }

        this.supportActionBar?.hide()

    }

    private fun goToMainScreen(){
        val intent = Intent(this@OnBoardingActivity, MainActivity::class.java)
        startActivity(intent)
        finish()
    }


    private fun something(){

        val sharedPref = applicationContext.getSharedPreferences("signInState", Context.MODE_PRIVATE)
        sharedPref.registerOnSharedPreferenceChangeListener(SharedPreferences.OnSharedPreferenceChangeListener { sharedPreferences, key ->

            if(key.equals("finished")){
                if(googleAuthUiClient.isSignedIn()){
                    val intent = Intent(this@OnBoardingActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                    Toast.makeText(applicationContext, "checked the change in value", Toast.LENGTH_SHORT).show()
                }
            }

        })

    }

}