package com.github.weg_li_android.data.api

import com.github.weg_li_android.data.model.District
import retrofit2.Call
import retrofit2.http.GET

interface WegliEndpoints {

    @GET("/districts.json")
    fun getDistricts(): Call<List<District>>
}