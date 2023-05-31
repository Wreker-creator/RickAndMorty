package com.wreker.rickandmortyapp.fragments.userLogin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.core.widget.doAfterTextChanged
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.wreker.rickandmortyapp.R
import com.wreker.rickandmortyapp.activity.OnBoardingActivity
import com.wreker.rickandmortyapp.databinding.FragmentSignUpBinding
import com.wreker.rickandmortyapp.model.User
import com.wreker.rickandmortyapp.tools.Constants.Companion.firebaseAuth
import com.wreker.rickandmortyapp.tools.Resource
import com.wreker.rickandmortyapp.tools.toast
import com.wreker.rickandmortyapp.tools.validEmail
import com.wreker.rickandmortyapp.tools.validPassword
import com.wreker.rickandmortyapp.viewModel.ViewModel

class SignUpFragment : Fragment(R.layout.fragment_sign_up){

    private var _binding : FragmentSignUpBinding ?= null
    private val binding get() = _binding!!

    private lateinit var viewModel : ViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentSignUpBinding.bind(view)

        viewModel = (activity as OnBoardingActivity).onBoardingViewModel

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

            etEmailContainer.error = validEmail(etEmail.text.toString())
            etPasswordContainer.error = validPassword(etPassword.text.toString())
            etConfirmPasswordContainer.error = validateConfirmPassword(etConfirmPassword.text.toString())

            if(etNameContainer.error != null || etEmailContainer.error != null || etPasswordContainer.error != null || etConfirmPasswordContainer.error != null) {

                activity?.toast("Please check your details!")

            }else{

                progressBar(true)

                if(etName.text!=null && etEmail.text!=null && etPassword.text!=null && etConfirmPassword.text!=null ){

                    val user = User(
                        displayName = etName.text.toString(),
                        email = etEmail.text.toString(),
                        firstName = null,
                        lastName = null,
                        phoneNumber = null,
                        birthday = null,
                        profileImage = null
                    )

                    viewModel.createUser(user,etPassword.text.toString())

                    viewModel.userSignUpStatus.observe(viewLifecycleOwner, Observer { authResult ->

                        when(authResult){

                            is Resource.Success -> {
                                goToHomeScreen(email = etEmail.text.toString(), password = etPassword.text.toString())
                                activity?.toast("Creating Account...")
                            }

                            is Resource.Loading -> {
                                progressBar(true)
                            }

                            is Resource.Error -> {
                                progressBar(false)
                                activity?.toast("Account creation failed! ${authResult.message}")
                            }

                        }

                    })

                } else{

                    activity?.toast("Please enter the details first!")

                }

            }

        }

    }

    private fun goToHomeScreen(email : String, password : String){

        viewModel.signInUser(email, password)

        viewModel.userSignUpStatus.observe(viewLifecycleOwner, Observer {authResult->

            when(authResult){

                is Resource.Loading -> {
                    progressBar(true)
                }

                is Resource.Success -> {
                    progressBar(false)
                    (activity as OnBoardingActivity).goToMainScreen()
                    activity?.toast("Enjoy :)")
                }

                is Resource.Error -> {
                    progressBar(false)
                    activity?.toast("Error! ${authResult.message}")
                }

            }

        })

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

            etName.setOnFocusChangeListener { _, hasFocus ->
                if(hasFocus){
                    etNameContainer.error = null
                }else{
                    if(etName.text.isNullOrBlank()){
                        etNameContainer.error = "Empty Field!"
                    }
                }
            }

        }

    }

    private fun emailFocusListener(){

        binding.apply {

            etEmail.setOnFocusChangeListener { v, hasFocus ->
                if(hasFocus){
                    etEmailContainer.error = null
                }else{
                    etEmailContainer.error = validEmail(etEmail.text.toString())
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