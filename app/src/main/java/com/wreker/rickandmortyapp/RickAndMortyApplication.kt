package com.wreker.rickandmortyapp

import android.app.Application
import android.content.Context

class RickAndMortyApplication : Application() {

    lateinit var context: Context
    override fun onCreate() {
        super.onCreate()
        context = this
    }

}