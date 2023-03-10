package com.wreker.rickandmortyapp.api

import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.wreker.rickandmortyapp.RickAndMortyApplication
import com.wreker.rickandmortyapp.tools.Constants
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory


//Retrofit - What does it do?
//retrofit is a type safe http client for android and java
//it make it easier for us to make networking requests
//we make a model class that represents our json response
//an interface that defines the networking requests or http operations
//and with the help of the Retrofit.builder() instance and builder api
//we combine these two above and define the URL endpoint for the http operations
//Also - It takes the converter that we provide to format the response

//Now, about this class
//it is a simple retrofit singleton class through which, we can make api requests
//from anywhere within our code

//keywords ->
//lazy - whatever code we put inside this block will only be initialized once hence creating
//       a single instance of retrofit throughout the entire app.
//httpLoggingInterceptor - this is to log the network response we get which would then show up
//                         in our logcat, the level is set to body for response's body
//Okhttp - this has some serious advantages, such as reduced request latency, http2 support
//         silent recovery from connection problems, support for synchronous asynchronous calls
//MOSHI - is a converter class, with which the requests and responses can be wrapped into
//        Java objects. Moshi is a little special though as it uses OKIO, who else uses OKIO?
//        Retrofit does. This use of okio by moshi results in less memory consumption and since
//        both retrofit and moshi use it this results in highly less memory consumption, faster
//        results when compared to GSON as a convertor factory. ALSO GSON IS HARMFUL FOR KOTLIN

//per class only one companion object is allowed which acts as a static variable and can
//be accessed without actually creating an object of the class

class RetrofitInstance() {
    companion object{

        private val retrofit by lazy {

            val moshi = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()

            Retrofit.Builder().baseUrl(Constants.baseUrl)
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .client(getLoggingHttpClient())
                .build()
        }

        private val rickAndMortyService : RickAndMortyApi by lazy {
            retrofit.create(RickAndMortyApi::class.java)
        }

        val apiClient = ApiClient(rickAndMortyService)


        private fun getLoggingHttpClient() : OkHttpClient{
            val builder = OkHttpClient.Builder()

            builder.addInterceptor(HttpLoggingInterceptor().apply {
                setLevel(HttpLoggingInterceptor.Level.BODY)
            })



            return builder.build()
        }

    }

}