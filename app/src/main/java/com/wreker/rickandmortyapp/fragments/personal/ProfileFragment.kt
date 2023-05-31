package com.wreker.rickandmortyapp.fragments.personal

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.squareup.picasso.Picasso
import com.wreker.rickandmortyapp.R
import com.wreker.rickandmortyapp.databinding.FragmentProfileBinding
import com.wreker.rickandmortyapp.tools.Constants.Companion.firebaseAuth
import com.wreker.rickandmortyapp.tools.Constants.Companion.firebaseDatabase


class ProfileFragment : Fragment(R.layout.fragment_profile){

    private var _binding : FragmentProfileBinding?=null
    private val binding get() = _binding!!
    private lateinit var currentUser : FirebaseUser

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        currentUser = firebaseAuth.currentUser!!
        _binding = FragmentProfileBinding.bind(view)

        binding.apply {

            Picasso.get().load(currentUser.photoUrl.toString()).into(imgProfile)
            ttvProfileDisplayName.text = currentUser.displayName.toString()

            firebaseDatabase.get()

        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}