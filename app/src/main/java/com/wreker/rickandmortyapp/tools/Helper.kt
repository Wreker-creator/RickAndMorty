package com.wreker.rickandmortyapp.tools

import android.content.Context
import android.text.Editable
import android.util.Patterns
import android.view.View
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar

fun validEmail(text: String): CharSequence? {

    if(text.isNotBlank()){
        if(!Patterns.EMAIL_ADDRESS.matcher(text).matches()){
            return "Invalid Email Address!"
        }
    }else{
        return "Empty Field!"
    }


    return null

}

fun validPassword(passWord : String): CharSequence? {

    if(passWord.isEmpty()){
        return "Empty Field!"
    }

    if(passWord.length <8){
        return "Minimum 8 characters required!"
    }

    if(!passWord.matches(".*[A-Z].*".toRegex())){
        return "Must contain one Upper Case Character!"
    }

    if(!passWord.matches(".*[a-z].*".toRegex())){
        return "Must contain one lower Case Character!"
    }

    if(!passWord.matches(".*\\d.*".toRegex())){
        return "Must contain one number!"
    }

    if(!passWord.matches(".*[@ |% !'#$^&*(){};:/?><.,=+~`].*".toRegex())){
        return "Must contain one Special Character!"
    }

    return null

}

fun Context.toast(message: String){
    Toast.makeText(this,message,Toast.LENGTH_SHORT).show()
}

fun View.snackbar(message : String){
    Snackbar.make(this, message, Snackbar.LENGTH_SHORT).show()
}

inline fun <T>safeCall(action : ()->com.wreker.rickandmortyapp.tools.Resource<T>) : com.wreker.rickandmortyapp.tools.Resource<T>{
    return try {
        action()
    }catch (e : Exception){
        com.wreker.rickandmortyapp.tools.Resource.Error(e.message ?: "An Unknown Error Occurred")
    }
}