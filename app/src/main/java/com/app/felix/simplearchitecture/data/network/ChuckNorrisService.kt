package com.app.felix.simplearchitecture.data.network

import com.app.felix.simplearchitecture.data.model.ChuckNorrisFact
import retrofit2.http.GET
import retrofit2.http.Query

interface ChuckNorrisService {

    @GET("random")
    suspend fun getRandomFact(): ChuckNorrisFact

    @GET("random")
    suspend fun getCategorizedRandomFact(
        @Query("category") category: String
    ): ChuckNorrisFact

    @GET("categories")
    suspend fun getCategories(): List<String>
}