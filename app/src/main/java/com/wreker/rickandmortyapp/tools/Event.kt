package com.wreker.rickandmortyapp.tools

//Google's recommended way to propagate events to the ui
open class Event<out T>(private val content : T){

    var hasBeenHandled = false
        private set //Allow external read but not write

    /*
    returns the content and prevents its use again
     */

    fun getContent(): T?{
        return if(hasBeenHandled){
            null
        }else{
            hasBeenHandled = true
            content
        }
    }

    /*
    returns the content even if it has already been handled
     */

    fun peekContent() : T = content



}