package com.wreker.rickandmortyapp.model

import com.wreker.rickandmortyapp.tools.Constants.Companion.firebaseAuth

data class User(

    val displayName : String?,
    val firstName : String?,
    val lastName : String?,
    val email : String,
    val phoneNumber : String?,
    val birthday : String?,
    val profileImage : String?

)
