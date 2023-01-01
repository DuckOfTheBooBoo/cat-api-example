package com.arajdianaltaf.dogapiexample

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

object RetrofitClient {

    val api: DogAPI by lazy {
        Retrofit.Builder()
            .baseUrl("https://dog.ceo")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(DogAPI::class.java)
    }

}