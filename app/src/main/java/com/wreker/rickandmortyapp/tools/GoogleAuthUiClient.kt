package com.wreker.rickandmortyapp.tools

import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.widget.Toast
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.wreker.rickandmortyapp.R
import com.wreker.rickandmortyapp.model.SignInResult
import com.wreker.rickandmortyapp.model.UserData
import com.wreker.rickandmortyapp.tools.Constants.Companion.firebaseAuth
import kotlinx.coroutines.tasks.await
import java.util.concurrent.CancellationException

class GoogleAuthUiClient(
    private val context : Context,
    private val oneTapClient : SignInClient
) {

    //because signing in can take a while so we want to suspend it.
    suspend fun signIn() : IntentSender?{
        val result = try {
            oneTapClient.beginSignIn(
                beginSignInRequest()
                //by default all these firebase function return task which runs asynchronously
                //after which we can use onSuccessListener or onFailureListener
                //however since we use a coroutine function we can simply wait this out until sign in is finished
            ).await()
        }catch (e : Exception){
            e.printStackTrace()
            if(e is CancellationException) throw e
            null
        }
        return result?.pendingIntent?.intentSender
    }

    suspend fun signInWithIntent(intent : Intent) : SignInResult{

        //something we get from the intent
        val credential = oneTapClient.getSignInCredentialFromIntent(intent)
        val googleIdToken = credential.googleIdToken
        val googleCredential = GoogleAuthProvider.getCredential(googleIdToken, null)

        return try {
            val user = firebaseAuth.signInWithCredential(googleCredential).await().user
            changeSignedInState(true)
            Toast.makeText(context, "Signed in Success", Toast.LENGTH_SHORT).show()
            SignInResult(
                data = user?.let {
                    UserData(
                        userId = it.uid,
                        userName = it.displayName,
                        profilePictureUrl = it.photoUrl?.toString()
                    )
                },
                errorMessage = null
            )

        }catch (e : Exception){
            changeSignedInState(false)
            e.printStackTrace()
            if(e is CancellationException) throw e
            SignInResult(
                data = null,
                errorMessage = e.localizedMessage
            )
        }

    }

    suspend fun signOut(){
        try {
            oneTapClient.signOut().await()
            firebaseAuth.signOut()
            changeSignedInState(false)
            Toast.makeText(context, "Signed out", Toast.LENGTH_SHORT).show()
        }catch (e : Exception){
            e.printStackTrace()
            if(e is CancellationException) throw e
        }
    }

    fun getSignedInUser() : UserData? = firebaseAuth.currentUser?.let {
        UserData(
            userId = it.uid,
            userName = it.displayName,
            profilePictureUrl = it.photoUrl?.toString()
        )
    }

    fun isSignedIn() : Boolean{
        val sharedPref = context.getSharedPreferences("signInState", Context.MODE_PRIVATE)
        return sharedPref.getBoolean("finished", false)
    }

    private fun changeSignedInState(bool : Boolean) {
        val sharedPref = context.getSharedPreferences("signInState", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putBoolean("finished",bool)
        editor.apply()
    }

    private fun beginSignInRequest() : BeginSignInRequest{
        return BeginSignInRequest.Builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true) //to say that this method of logging in is supported and we want to sign in via using these google ids
                    .setFilterByAuthorizedAccounts(false) // if true it checks to see if you are already signed in with an account and will only show that account as an option else will show all available google accounts
                    .setServerClientId(context.getString(R.string.webClientId))
                    .build()

            )
            .setAutoSelectEnabled(true) //if you only have one google account to sign in with it will automatically select that account.
            .build()
    }

}