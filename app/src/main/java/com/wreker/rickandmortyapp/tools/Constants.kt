package com.wreker.rickandmortyapp.tools

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase

class Constants {

    companion object{

        val firebaseAuth : FirebaseAuth = FirebaseAuth.getInstance()
        const val baseUrl = "https://rickandmortyapi.com/api/"
        const val pageSize = 20
        const val prefetchDistance = pageSize*2
        const val INTENT_EXTRA_CHARACTER_ID = "character_id"


    }

}

