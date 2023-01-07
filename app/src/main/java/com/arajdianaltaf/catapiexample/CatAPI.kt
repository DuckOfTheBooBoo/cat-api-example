package com.arajdianaltaf.catapiexample

import com.arajdianaltaf.catapiexample.data.Breed
import com.arajdianaltaf.catapiexample.data.CatResponseImageItem
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface CatAPI {

    @GET("/v1/images/search")
    suspend fun getCatImagesByBreed(
        @Query("api_key") api_key: String,
        @Query("breed_ids") breedId: String,
        @Query("limit") limit: Int = 6
    ): Response<List<CatResponseImageItem>>
}