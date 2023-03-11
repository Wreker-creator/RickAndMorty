package com.wreker.rickandmortyapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import com.wreker.rickandmortyapp.R
import com.wreker.rickandmortyapp.databinding.FragmentEpisodeListBinding

class EpisodeListFragment : Fragment(R.layout.fragment_episode_list) {

    private var _binding : FragmentEpisodeListBinding?=null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentEpisodeListBinding.bind(view)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}