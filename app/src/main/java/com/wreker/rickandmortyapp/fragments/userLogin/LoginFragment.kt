package com.wreker.rickandmortyapp.fragments.userLogin

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.common.internal.service.Common
import com.wreker.rickandmortyapp.R
import com.wreker.rickandmortyapp.activity.MainActivity
import com.wreker.rickandmortyapp.activity.OnBoardingActivity
import com.wreker.rickandmortyapp.databinding.FragmentLoginBinding
import com.wreker.rickandmortyapp.tools.Constants.Companion.firebaseAuth
import com.wreker.rickandmortyapp.tools.GoogleAuthUiClient
import com.wreker.rickandmortyapp.tools.toast
import com.wreker.rickandmortyapp.tools.validEmail
import kotlinx.coroutines.launch

class LoginFragment : Fragment(R.layout.fragment_login) {

    private var _binding : FragmentLoginBinding?= null
    private val binding get() = _binding!!

    private val googleAuthUiClient by lazy {
        GoogleAuthUiClient(
            context = requireContext(),
            oneTapClient = Identity.getSignInClient(requireContext())
        )
    }

    private val launcher = registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()){

            result ->

        when(result.resultCode){

            AppCompatActivity.RESULT_OK  ->{
                lifecycleScope.launch {

                    googleAuthUiClient.signInWithIntent(
                        intent = result.data ?: return@launch
                    )

                }
            }

            AppCompatActivity.RESULT_CANCELED -> {
                activity?.toast("Cancelled")
            }

            AppCompatActivity.RESULT_FIRST_USER -> {
                activity?.toast("first time user??")
            }
//
//            CommonStatusCodes.SUCCESS -> {
//
//                lifecycleScope.launch {
//
//                    googleAuthUiClient.signInWithIntent(
//                        intent = result.data?:return@launch
//                    )
//
//                }
//            }

            CommonStatusCodes.API_NOT_CONNECTED -> {
                activity?.toast("Api not connected.")
            }

            CommonStatusCodes.CANCELED -> {
                activity?.toast("Login Attempt was Cancelled")
            }

            CommonStatusCodes.CONNECTION_SUSPENDED_DURING_CALL -> {
                activity?.toast("Connection suspended during call")
            }

            CommonStatusCodes.DEVELOPER_ERROR -> {
                activity?.toast("Developer Error")
            }

            CommonStatusCodes.ERROR -> {
                activity?.toast("An error occurred! Please Try Again Later!")
            }

            CommonStatusCodes.INTERNAL_ERROR -> {
                activity?.toast("An internal error occurred! Please Try Again Later!")
            }

            CommonStatusCodes.INTERRUPTED ->{
                activity?.toast("Something interrupted the request. Please Try Again Later!")
            }

            CommonStatusCodes.INVALID_ACCOUNT -> {
                activity?.toast("Invalid account!")
            }

            CommonStatusCodes.NETWORK_ERROR -> {
                activity?.toast("Network Error! Please Try Again Later!")
            }

            CommonStatusCodes.RECONNECTION_TIMED_OUT -> {
                activity?.toast("Connection timed out!")
            }

            CommonStatusCodes.RECONNECTION_TIMED_OUT_DURING_UPDATE -> {
                activity?.toast("The connection timed-out while waiting for Google Play services to update.")
            }

            CommonStatusCodes.REMOTE_EXCEPTION -> {
                activity?.toast("Remote Exception please Try Again Later!")
            }

            CommonStatusCodes.SIGN_IN_REQUIRED -> {
                activity?.toast("The client attempted to connect to the service but the user is not signed in.")
            }

            //need to check what the fuck is happening here !!!!
//            CommonStatusCodes.SUCCESS -> {
//
//                activity?.toast("Login Success!")
//
//            }
//
//            CommonStatusCodes.SUCCESS_CACHE -> {
//                activity?.toast("The operation was successful, but was used the device's cache.")
//            }

            CommonStatusCodes.TIMEOUT -> {
                activity?.toast("Network Timed out while attempting to login!")
            }

            else -> {
                activity?.toast("We don't know the fuck the error was!")
            }

        }

    }

    override fun onStart() {
        super.onStart()

        if(firebaseAuth.currentUser!=null){
            (activity as OnBoardingActivity).goToMainScreen()
            activity?.toast("Still Logged In")
        }else (
            activity?.toast("Not Logged In")
        )

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentLoginBinding.bind(view)

        binding.apply {

            btnRegister.setOnClickListener {

                findNavController().navigate(R.id.action_loginFragment_to_signUpFragment)

            }

            btnLoginWithGoogle.setOnClickListener {

                lifecycleScope.launch {

                    val signInIntentSender = googleAuthUiClient.signIn()
                    launcher.launch(
                        IntentSenderRequest.Builder(
                            signInIntentSender ?: return@launch
                        ).build()
                    )

                }

            }

            btnLogin.setOnClickListener {

                if(etEmail.text.isNullOrBlank()){

                    etEmailContainer.error = "Empty Field!"

                }else if(etPassword.text.isNullOrBlank()){

                    etPasswordContainer.error = "Empty Field!"

                }else{

                    handleLogin()

                }

                checkIfSignedIn()

            }



        }



        passwordFocusListener()
        emailFocusListener()

    }

    private fun checkIfSignedIn(){

        val sharedPref = requireContext().getSharedPreferences("signInState", Context.MODE_PRIVATE)
        sharedPref.registerOnSharedPreferenceChangeListener(SharedPreferences.OnSharedPreferenceChangeListener { _, key ->

            if(key.equals("finished")){
                if(googleAuthUiClient.isSignedIn()){
                    (activity as OnBoardingActivity).goToMainScreen()
                    activity?.toast("Checked the change in value")
                }
            }

        })

    }

    private fun handleLogin() {

        if(binding.etEmailContainer.error == null && binding.etPasswordContainer.error == null){

            progressBar(true)

            firebaseAuth.signInWithEmailAndPassword(binding.etEmail.text.toString(), binding.etPassword.text.toString()).addOnCompleteListener {

                if(it.isSuccessful) {

                    (activity as OnBoardingActivity).goToMainScreen()
                    activity?.toast("Login success!")
                    progressBar(false)

                }

            }.addOnCanceledListener {

                progressBar(false)
                activity?.toast("Login Canceled!")

            }.addOnFailureListener {failure ->

                progressBar(false)
                activity?.toast("Account creation failed\n" +
                        " ${failure.message}")

            }

        }else{

            progressBar(false)
            activity?.toast("Error! Please Check your Credential!")

        }

    }

    private fun passwordFocusListener(){

        binding.apply {

            etPassword.setOnFocusChangeListener { v, hasFocus ->

                if(hasFocus){

                    etPasswordContainer.error = null

                }

            }

            etPassword.setOnClickListener {
                etPasswordContainer.error = null
            }

        }

    }

    private fun emailFocusListener(){

        binding.apply {

            etEmail.setOnFocusChangeListener { v, hasFocus ->

                if(!hasFocus){
                    etEmailContainer.error = validEmail(etEmail.text.toString())
                }

                if(hasFocus){
                    etEmailContainer.error = null
                }

            }

        }

    }

    private fun progressBar(bool : Boolean){

        if(bool){

            binding.progressBar.visibility = View.VISIBLE
            binding.progressBackground.visibility = View.VISIBLE

        }else{

            binding.progressBar.visibility = View.GONE
            binding.progressBackground.visibility = View.GONE

        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}