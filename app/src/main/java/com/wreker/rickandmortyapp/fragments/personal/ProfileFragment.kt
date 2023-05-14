package com.wreker.rickandmortyapp.fragments.personal

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.wreker.rickandmortyapp.R
import com.wreker.rickandmortyapp.databinding.FragmentProfileBinding


class ProfileFragment : Fragment(R.layout.fragment_profile){

    private var _binding : FragmentProfileBinding?=null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentProfileBinding.bind(view)



    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}