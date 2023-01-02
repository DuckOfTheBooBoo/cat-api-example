package com.arajdianaltaf.catapiexample.data

data class CatResponseImageItem(
    val breeds: List<Breed>,
    val height: Int,
    val id: String,
    val url: String,
    val width: Int
)