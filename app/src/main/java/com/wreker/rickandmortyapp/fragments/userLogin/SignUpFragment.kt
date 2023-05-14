package com.wreker.rickandmortyapp.fragments.userLogin

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.navigation.fragment.findNavController
import com.wreker.rickandmortyapp.R
import com.wreker.rickandmortyapp.activity.OnBoardingActivity
import com.wreker.rickandmortyapp.databinding.FragmentLoginBinding
import com.wreker.rickandmortyapp.databinding.FragmentSignUpBinding
import com.wreker.rickandmortyapp.tools.Constants.Companion.firebaseAuth
import com.wreker.rickandmortyapp.tools.toast
import com.wreker.rickandmortyapp.tools.validEmail
import com.wreker.rickandmortyapp.tools.validPassword

class SignUpFragment : Fragment(R.layout.fragment_sign_up){

    private var _binding : FragmentSignUpBinding ?= null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentSignUpBinding.bind(view)

        binding.apply {

            btnLogin.setOnClickListener {

                findNavController().navigate(R.id.action_signUpFragment_to_loginFragment)

            }

            btnSignUp.setOnClickListener{

                if(etName.text.isNullOrBlank()){

                    etNameContainer.error = "Empty Field"

                }else if(etEmail.text.isNullOrBlank()){

                    etEmailContainer.error = "Empty Field"

                }else if(etPassword.text.isNullOrBlank()){

                    etPasswordContainer.error = "Empty Field"

                }else if(etConfirmPassword.text.isNullOrBlank()){

                    etConfirmPasswordContainer.error = "Empty Field"

                }else{

                    handleSignUp()

                }

            }

        }

        emailFocusListener()
        passwordFocusListener()
        nameFocusListener()
        confirmPasswordFocusListener()

    }

    private fun handleSignUp() {

        binding.apply {

            if(etNameContainer.error != null || etEmailContainer.error != null || etPasswordContainer.error != null || etConfirmPasswordContainer.error != null) {

                activity?.toast("Please check your details!")

            }else{

                if(etName.text!=null && etEmail.text!=null && etPassword.text!=null && etConfirmPassword.text!=null ){

                    progressBar(true)

                    firebaseAuth.createUserWithEmailAndPassword(etEmail.text.toString(), etPassword.text.toString()).addOnCompleteListener {authResult ->

                        if(authResult.isSuccessful){

                            progressBar(false)
                            goToHomeScreen(email = etEmail.text.toString(), password = etPassword.text.toString())
                            activity?.toast("Creating Account...")

                        }

                        authResult.exception?.let {exception ->

                            Log.e("Creating Account", exception.message.toString())

                        }

                    }.addOnFailureListener {failure ->

                        progressBar(false)
                        activity?.toast("Account creation failed\n" +
                                " ${failure.message}")

                    }

                } else{

                    activity?.toast("Please enter the details first!")

                }

            }

        }

    }

    private fun goToHomeScreen(email : String, password : String){

        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener {authResult ->

            if(authResult.isSuccessful){

                (activity as OnBoardingActivity).goToMainScreen()
                activity?.toast("Enjoy :)")

            }

        }.addOnCanceledListener {

            activity?.toast("Login Cancelled!")

        }.addOnFailureListener {

            activity?.toast("Error! Login Failed, Please try Again.")

        }

    }

    private fun confirmPasswordFocusListener() {

        binding.apply {

            etConfirmPassword.doOnTextChanged { text, start, before, count ->

                etConfirmPasswordContainer.error = validateConfirmPassword(text.toString())

            }

        }

    }

    private fun nameFocusListener(){

        binding.apply {

            etName.setOnFocusChangeListener { v, hasFocus ->

                if(!hasFocus){

                    if(etName.text.isNullOrBlank()){

                        etNameContainer.error = "Empty Field!"

                    }

                }else{

                    etNameContainer.error = null

                }

            }

        }

    }

    private fun emailFocusListener(){

        binding.apply {

            etEmail.setOnFocusChangeListener { v, hasFocus ->

                if(!hasFocus){

                    etEmailContainer.error = validEmail(etEmail.text.toString())

                }else{

                    etEmailContainer.error = null

                }

            }

        }

    }

    private fun passwordFocusListener(){

        binding.apply {

            etPassword.setOnFocusChangeListener { v, hasFocus ->
                if(hasFocus){
                    etPasswordContainer.error = null
                }else{
                    etPasswordContainer.error = validPassword(etPassword.text.toString())
                }
            }

        }

    }

    private fun validateConfirmPassword(text: String) : CharSequence?{

        if(text != binding.etPassword.text.toString()){
            return "Passwords do not match!"
        }

        return null

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