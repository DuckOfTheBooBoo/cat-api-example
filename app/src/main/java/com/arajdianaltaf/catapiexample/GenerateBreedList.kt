package com.arajdianaltaf.catapiexample

import com.arajdianaltaf.catapiexample.data.CatResponseItem
import com.google.gson.Gson
import java.io.FileReader

private fun parseJson(): List<CatResponseItem> {
    val gson = Gson()

    return gson.fromJson(
        FileReader("app/src/main/java/com/arajdianaltaf/catapiexample/breeds.json"),
        Array<CatResponseItem>::class.java
    ).toList()

}

suspend fun getBreedList(): List<String> {

    val jsonBreeds = parseJson()

    val breedList = mutableListOf<String>()

    for (cat in jsonBreeds) {
        breedList.add(cat.id)
    }

    return breedList
}