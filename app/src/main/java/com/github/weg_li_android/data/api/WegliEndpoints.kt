package com.github.weg_li_android.data.api

import com.github.weg_li_android.data.model.District
import retrofit2.Response
import retrofit2.http.GET

interface WegliEndpoints {

    @GET("/districts.json")
    suspend fun getDistricts(): Response<List<District>>
}