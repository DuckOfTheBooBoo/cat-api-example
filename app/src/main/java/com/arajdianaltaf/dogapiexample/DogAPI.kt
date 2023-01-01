package com.arajdianaltaf.dogapiexample

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface DogAPI {

    @GET("/api/breed/{breed}/images/random/{num}")
    suspend fun getDogImagesByBreed(@Path("breed") breed: String, @Path("num") num: Int = 6): Response<ResponseImages>

    @GET("/api/breed/{breed}/{subBreed}/images/random/{num}")
    suspend fun getDogImagesBySubBreed(@Path("breed") breed: String, @Path("subBreed") subBreed: String, @Path("num") num: Int = 6): Response<ResponseImages>

}